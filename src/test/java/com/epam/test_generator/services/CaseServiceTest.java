package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.EditCaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.state.machine.StateMachineAdapter;
import com.epam.test_generator.transformers.CaseTransformer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.statemachine.StateMachine;

@RunWith(MockitoJUnitRunner.class)
public class CaseServiceTest {

    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;

    private Suit suit;

    private Case caze;
    private CaseDTO expectedCase;

    private List<Step> listSteps = new ArrayList<>();
    private List<StepDTO> expectedListSteps = new ArrayList<>();
    private Set<Tag> setOfTags = new HashSet<>();
    private Set<TagDTO> expectedSetTags = new HashSet<>();

    @Mock
    private SuitDAO suitDAO;

    @Mock
    private CaseDAO caseDAO;

    @Mock
    private CaseVersionDAO caseVersionDAO;

    @Mock
    private CaseTransformer caseTransformer;

    @InjectMocks
    private CaseService caseService;

    @Mock
    private StateMachineAdapter stateMachineAdapter;

    @Mock
    private StateMachine<Status, Event> stateMachine;

    @Before
    public void setUp() {
        final List<Case> listCases = new ArrayList<>();

        listCases.add(new Case(1L, "name 1", "Case 1", listSteps, 1, setOfTags));
        listCases.add(new Case(2L, "name 2", "Case 2", listSteps, 2, setOfTags));

        caze = new Case(SIMPLE_CASE_ID, "Case name", "Case desc", listSteps, 1, setOfTags);
        expectedCase = new CaseDTO(SIMPLE_CASE_ID, "Case name", "Case desc", expectedListSteps, 1,
            expectedSetTags, Status.NOT_DONE);
        suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", listCases, 1, setOfTags);
    }

    @Test
    public void getCaseTest() throws Exception {
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseTransformer.toDto(any(Case.class))).thenReturn(expectedCase);

        final CaseDTO actualCase = caseService.getCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
        assertEquals(expectedCase, actualCase);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(caseTransformer).toDto(any(Case.class));
    }

    @Test(expected = NotFoundException.class)
    public void getCaseTest_expectNotFoundExceptionFromSuit() {
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        caseService.getCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getCaseTest_expectNotFoundExceptionFromCase() {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(null);

        caseService.getCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test
    public void addCaseToSuitTest() throws Exception {
        final Case newCase = new Case(3L, "Case name", "Case 3", listSteps, 2, setOfTags);
        final CaseDTO newCaseDTO = new CaseDTO(null, "Case name", "Case 3", expectedListSteps, 2,
            expectedSetTags, Status.NOT_DONE);

        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseTransformer.fromDto(any(CaseDTO.class))).thenReturn(newCase);
        when(caseDAO.save(any(Case.class))).thenReturn(newCase);

        final Long actualId = caseService.addCaseToSuit(SIMPLE_SUIT_ID, newCaseDTO);
        assertEquals(newCase.getId(), actualId);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseTransformer).fromDto(any(CaseDTO.class));
        verify(caseDAO).save(any(Case.class));
        verify(caseVersionDAO).save(eq(newCase));
    }

    @Test(expected = NotFoundException.class)
    public void addCaseToSuitTest_expectNotFoundExceptionFromSuit() {
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        caseService.addCaseToSuit(SIMPLE_SUIT_ID, new CaseDTO());
    }

    @Test
    public void updateCaseTest() throws Exception {
        EditCaseDTO updateCaseDTO = new EditCaseDTO("New Case desc", null);

        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);

        caseService.updateCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, updateCaseDTO);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(caseDAO).save(eq(caze));
        verify(caseVersionDAO).save(eq(caze));
    }

    @Test(expected = NotFoundException.class)
    public void updateCaseTest_expectNotFoundExceptionFromSuit() {
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        caseService.updateCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new EditCaseDTO());
    }

    @Test(expected = NotFoundException.class)
    public void updateCaseTest_expectNotFoundExceptionFromCase() {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(null);

        caseService.updateCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new EditCaseDTO());
    }

    @Test
    public void removeCaseTest() throws Exception {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        doNothing().when(caseDAO).delete(caze);

        caseService.removeCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).delete(eq(SIMPLE_CASE_ID));
        verify(caseVersionDAO).delete(eq(caze));
    }

    @Test(expected = NotFoundException.class)
    public void removeCaseTest_expectNotFoundExceptionFromSuit() {
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        caseService.removeCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test(expected = NotFoundException.class)
    public void removeCaseTest_expectNotFoundExceptionFromCase() {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(null);

        caseService.removeCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test
    public void removeCasesTest() throws Exception {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        List<Long> deleteCaseIds = Arrays.asList(1L, 2L);

        caseService.removeCases(SIMPLE_SUIT_ID, deleteCaseIds);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).delete(eq(1L));
        verify(caseDAO).delete(eq(2L));
        verify(caseVersionDAO, times(2)).delete(any(Case.class));
    }

    @Test(expected = NotFoundException.class)
    public void removeCasesTest_expectNotFoundExceptionFromSuit() {
        List<Long> caseIds = new ArrayList<>();

        when(suitDAO.findOne(anyLong())).thenReturn(null);

        caseIds.add(SIMPLE_CASE_ID);
        caseService.removeCases(SIMPLE_SUIT_ID, caseIds);
    }

    @Test
    public void performEventTest_changingStatusFromNotDoneToNotRun() throws Exception {
        when(caseTransformer.fromDto(any(CaseDTO.class))).thenReturn(caze);
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stateMachine.sendEvent(any(Event.class))).thenReturn(true);

        when(stateMachineAdapter.restore(caze)).thenReturn(stateMachine);

        caze.setStatus(Status.NOT_DONE);

        caseService.performEvent(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, Event.CREATE);

        verify(stateMachineAdapter).persist(eq(stateMachine), eq(caze));
        verify(caseVersionDAO).save(eq(caze));
    }

    @Test(expected = BadRequestException.class)
    public void performEventTest_sendEventFalse() throws Exception {

        when(caseTransformer.fromDto(any(CaseDTO.class))).thenReturn(caze);
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stateMachine.sendEvent(any(Event.class))).thenReturn(false);

        when(stateMachineAdapter.restore(caze)).thenReturn(stateMachine);

        caseService.performEvent(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, Event.CREATE);
    }
}