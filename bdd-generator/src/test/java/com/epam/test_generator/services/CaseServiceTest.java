package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.caze.CaseTransformer;
import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.request.CaseEditDTO;
import com.epam.test_generator.controllers.caze.request.CaseUpdateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.suit.SuitTransformer;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.controllers.version.caze.CaseVersionTransformer;
import com.epam.test_generator.controllers.version.caze.response.CaseVersionDTO;
import com.epam.test_generator.controllers.version.caze.response.PropertyDifferenceDTO;
import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.entities.Action;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.pojo.CaseVersion;
import com.epam.test_generator.pojo.PropertyDifference;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.state.machine.StateMachineAdapter;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RunWith(MockitoJUnitRunner.class)
public class CaseServiceTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 3L;
    private static final String SIMPLE_COMMIT_ID = "1.5";

    private Suit suit;

    private Case caze;
    private Case updatedCase;
    private CaseDTO expectedCaseDTO;

    private CaseEditDTO caseEditDTO;
    private CaseCreateDTO caseCreateDTO;
    private CaseUpdateDTO caseUpdateDTO;

    private List<CaseVersion> caseVersions;
    private List<CaseVersionDTO> expectedCaseVersions;
    private List<Step> listSteps = new ArrayList<>();
    private List<StepDTO> expectedListSteps = new ArrayList<>();
    private Set<Tag> setOfTags = new HashSet<>();
    private Set<TagDTO> expectedSetTags = new HashSet<>();

    @Mock
    private CaseDAO caseDAO;

    @Mock
    private CaseVersionDAO caseVersionDAO;

    @Mock
    private SuitVersionDAO suitVersionDAO;

    @Mock
    private SuitService suitService;

    @Mock
    private CaseTransformer caseTransformer;

    @Mock
    private SuitTransformer suitTransformer;

    @Mock
    private CaseVersionTransformer caseVersionTransformer;

    @InjectMocks
    private CaseService caseService;

    @Mock
    private StateMachineAdapter stateMachineAdapter;

    @Mock
    private StateMachine<Status, Event> stateMachine;

    @Before
    public void setUp() {
        List<Case> listCases = new ArrayList<>();

        Case case1 = new Case(1L, "name 1", "case 1",
            listSteps, 1, setOfTags, "comment 1");
        case1.setRowNumber(1);
        Case case2 = new Case(2L, "name 2", "case 2",
            listSteps, 2, setOfTags, "comment 2");
        case2.setRowNumber(2);
        listCases.add(case1);
        listCases.add(case2);

        caze = new Case(SIMPLE_CASE_ID, "case name", "case desc",
            listSteps, 1, setOfTags, "comment");
        caze.setRowNumber(1);

        listCases.add(caze);
        expectedCaseDTO = new CaseDTO(SIMPLE_CASE_ID, "case name", "case desc",
            expectedListSteps, 1, expectedSetTags, Status.NOT_DONE.getStatusName(), "comment", 1);
        expectedCaseDTO.setRowNumber(1);
        caseCreateDTO = new CaseCreateDTO("case name", "case desc",
               1, "comment", expectedSetTags);
        caseUpdateDTO = new CaseUpdateDTO(caze.getName(), caze.getDescription(), caze.getPriority(),
                caze.getStatus(), caze.getComment());
        updatedCase = new Case(SIMPLE_CASE_ID, "case name", "case desc",
                listSteps, 1, setOfTags, "comment");
        updatedCase.setRowNumber(1);
        suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc",
            listCases, 1, setOfTags, 1);

        caseVersions = new ArrayList<>();
        expectedCaseVersions = new ArrayList<>();

        StepDTO stepDTO = new StepDTO();
        PropertyDifference propertyDifference1 = new PropertyDifference("1", null, "3");
        PropertyDifference propertyDifference2 = new PropertyDifference("2", "1", "2");
        PropertyDifference propertyDifference3 = new PropertyDifference("3", null, stepDTO);
        PropertyDifference propertyDifference4 = new PropertyDifference("4", stepDTO, stepDTO);

        PropertyDifferenceDTO propertyDifferenceDTO1 =
            new PropertyDifferenceDTO("1", null, "3");
        PropertyDifferenceDTO propertyDifferenceDTO2 =
            new PropertyDifferenceDTO("2", "1", "2");
        PropertyDifferenceDTO propertyDifferenceDTO3 =
            new PropertyDifferenceDTO("1", null, stepDTO);
        PropertyDifferenceDTO propertyDifferenceDTO4 =
            new PropertyDifferenceDTO("2", stepDTO, stepDTO);

        caseVersions.add(new CaseVersion("1.3", new Date(), "author",
            Lists.newArrayList(propertyDifference1, propertyDifference2)));
        caseVersions.add(new CaseVersion("2.4", new Date(), "autho2",
            Lists.newArrayList()));
        caseVersions.add(new CaseVersion("3.5", new Date(), "author3",
            Lists.newArrayList(propertyDifference3, propertyDifference4)));
        caseVersions.add(new CaseVersion("4.6", new Date(), "autho4",
            Lists.newArrayList()));

        expectedCaseVersions.add(new CaseVersionDTO("1.3", "", "author",
            Lists.newArrayList(propertyDifferenceDTO1, propertyDifferenceDTO2)));
        expectedCaseVersions.add(new CaseVersionDTO("2.4", "", "autho2",
            Lists.newArrayList()));
        expectedCaseVersions.add(new CaseVersionDTO("3.5", "", "author3",
            Lists.newArrayList(propertyDifferenceDTO3, propertyDifferenceDTO4)));
        expectedCaseVersions.add(new CaseVersionDTO("4.6", "", "autho4",
            Lists.newArrayList()));
    }

    @Test
    public void get_Case_Success() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.of(caze));
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);

        Case actualCase = caseService
            .getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
        assertEquals(caze, actualCase);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findById(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void get_CaseDTO_Success() {
        when(caseTransformer.toDto(any())).thenReturn(expectedCaseDTO);
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.of(caze));
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);

        CaseDTO actualCaseDTO = caseService
            .getCaseDTO(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
        assertEquals(expectedCaseDTO, actualCaseDTO);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findById(eq(SIMPLE_CASE_ID));
    }

    @Test(expected = NotFoundException.class)
    public void get_Case_expectNotFoundExceptionFromSuit() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(null);

        caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test(expected = NotFoundException.class)
    public void get_Case_expectNotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.empty());

        caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test
    public void add_CaseToSuit_Success() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseTransformer.fromDto(any(CaseCreateDTO.class))).thenReturn(caze);
        when(caseDAO.save(any(Case.class))).thenReturn(caze);
        when(caseTransformer.toDto(any(Case.class))).thenReturn(expectedCaseDTO);

        CaseDTO actualCaseDTO = caseService
            .addCaseToSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, caseCreateDTO);
        assertEquals(expectedCaseDTO, actualCaseDTO);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseTransformer).fromDto(eq(caseCreateDTO));
        verify(caseDAO).save(eq(caze));
        verify(caseVersionDAO).save(eq(caze));
        verify(caseTransformer).toDto(eq(caze));
    }

    @Test(expected = NotFoundException.class)
    public void add_CaseToSuit_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(suitService).getSuit(anyLong(), anyLong());
        caseService.addCaseToSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, new CaseCreateDTO());
    }

    @Test
    public void update_Case_Success() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.of(caze));
        when(caseDAO.save(caze)).thenReturn(caze);
        when(caseTransformer.updateFromDto(caseUpdateDTO, caze)).thenReturn(updatedCase);
        when(caseTransformer.toDto(caze)).thenReturn(expectedCaseDTO);


        CaseDTO actualCaseDTO =
            caseService.updateCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, caseUpdateDTO);

        assertEquals(actualCaseDTO, expectedCaseDTO);
        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findById(eq(SIMPLE_CASE_ID));
        verify(caseDAO).save(eq(caze));
        verify(caseTransformer).toDto(eq(caze));
        verify(caseVersionDAO).save(eq(caze));
    }

    @Test(expected = NotFoundException.class)
    public void update_Case_NotFoundExceptionFromSuit() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(null);

        caseService
            .updateCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new CaseUpdateDTO());
    }

    @Test(expected = NotFoundException.class)
    public void update_Case_NotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.empty());

        caseService
            .updateCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new CaseUpdateDTO());
    }

    @Test
    public void remove_Case_Success() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.of(caze));
        doNothing().when(caseDAO).delete(caze);
        doNothing().when(caseVersionDAO).delete(caze);
        when(caseTransformer.toDto(any(Case.class))).thenReturn(expectedCaseDTO);

        CaseDTO actualRemovedCaseDTO = caseService
            .removeCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
        assertEquals(expectedCaseDTO, actualRemovedCaseDTO);

        verify(caseDAO).findById(eq(SIMPLE_CASE_ID));
        verify(caseDAO).delete(caze);
        verify(caseVersionDAO).delete(eq(caze));
        verify(caseTransformer).toDto(eq(caze));
    }

    @Test(expected = NotFoundException.class)
    public void remove_Case_NotFoundExceptionFromSuit() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(null);

        caseService.removeCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test(expected = NotFoundException.class)
    public void remove_Case_NotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.empty());

        caseService.removeCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test
    public void remove_Cases_Success() {
        List<CaseDTO> expectedRemovedCasesDTO = new ArrayList<>();
        expectedRemovedCasesDTO.add(new CaseDTO(1L, "name 1", "case 1",
            expectedListSteps, 1, expectedSetTags,
            Status.NOT_RUN.getStatusName(), "comment 1", 1));
        expectedRemovedCasesDTO.add(new CaseDTO(2L, "name 2", "case 2",
            expectedListSteps, 2, expectedSetTags,
            Status.NOT_RUN.getStatusName(), "comment 2", 2));

        List<Long> deleteCaseIds = Arrays.asList(1L, 2L);

        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseTransformer.toDtoList(anyList()))
            .thenReturn(expectedRemovedCasesDTO);

        List<CaseDTO> actualRemovedCasesDTO = caseService
            .removeCases(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, deleteCaseIds);

        assertEquals(expectedRemovedCasesDTO, actualRemovedCasesDTO);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));

        verify(caseDAO).deleteById(eq(1L));
        verify(caseDAO).deleteById(eq(2L));
        verify(caseVersionDAO, times(2)).delete(any(Case.class));
        verify(caseTransformer).toDtoList(anyList());
    }

    @Test(expected = NotFoundException.class)
    public void remove_Cases_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(suitService).getSuit(anyLong(), anyLong());
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(null);

        List<Long> caseIds = new ArrayList<>();
        caseIds.add(SIMPLE_CASE_ID);
        caseService.removeCases(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, caseIds);
    }

    @Test
    public void getCaseVersions_SimpleCase_ReturnExpectedCaseVersionDTOs() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.of(caze));
        when(caseVersionDAO.findAll(anyLong())).thenReturn(caseVersions);
        when(caseVersionTransformer.toListDto(anyList()))
            .thenReturn(expectedCaseVersions);

        List<CaseVersionDTO> caseVersionDTOs = caseService
            .getCaseVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);

        assertEquals(expectedCaseVersions, caseVersionDTOs);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findById(SIMPLE_CASE_ID);
        verify(caseVersionDAO).findAll(SIMPLE_CASE_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getCaseVersions_NullSuit_NotFoundException() {
        doThrow(NotFoundException.class).when(suitService).getSuit(anyLong(), anyLong());

        caseService.getCaseVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getCaseVersions_NullCase_NotFoundException() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.empty());

        caseService.getCaseVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test(expected = BadRequestException.class)
    public void getCaseVersions_CaseDoesNotBelongsToSuit_BadRequestException() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.of(new Case()));

        caseService.getCaseVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test
    public void restoreCase_SimpleCase_Restored() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.of(caze));
        when(caseVersionDAO.findByCommitId(anyLong(), anyString())).thenReturn(caze);
        when(caseDAO.save(caze)).thenReturn(caze);
        when(caseTransformer.toDto(caze)).thenReturn(expectedCaseDTO);

        CaseDTO actualRestoreCaseDTO = caseService
            .restoreCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
        assertEquals(expectedCaseDTO, actualRestoreCaseDTO);

        verify(caseDAO).findById(SIMPLE_CASE_ID);
        verify(caseVersionDAO).findByCommitId(SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
        verify(caseDAO).save(eq(caze));
        verify(caseVersionDAO).save(eq(caze));
        verify(caseTransformer).toDto(eq(caze));
    }

    @Test(expected = NotFoundException.class)
    public void restoreCase_NullSuit_NotFoundException() {
        doThrow(NotFoundException.class).when(suitService).getSuit(anyLong(), anyLong());

        caseService
            .restoreCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void restoreCaseByCaseVersionId_NullCase_NotFoundException() {
        when(caseDAO.findById(anyLong())).thenReturn(Optional.empty());

        caseService
            .restoreCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
    }

    @Test(expected = BadRequestException.class)
    public void restoreCase_CaseDoesNotBelongsToSuit_BadRequestException() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.of(new Case()));

        caseService
            .restoreCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void restoreCase_NullCaseToRestore_NotFoundException() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.of(caze));
        when(caseVersionDAO.findByCommitId(anyLong(), anyString())).thenReturn(null);

        caseService
            .restoreCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
    }

    @Test
    public void performEvent_StatusFromNotDoneToNotRun_Valid() throws Exception {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.of(caze));
        when(stateMachine.sendEvent(any(Event.class))).thenReturn(true);

        when(stateMachineAdapter.restore(caze)).thenReturn(stateMachine);

        caze.setStatus(Status.NOT_DONE);

        caseService.performEvent(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, Event.CREATE);

        verify(stateMachineAdapter).persist(eq(stateMachine), eq(caze));
        verify(caseVersionDAO).save(eq(caze));
    }

    @Test(expected = BadRequestException.class)
    public void perform_Event_BadRequestException() throws Exception {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findById(anyLong())).thenReturn(Optional.of(caze));
        when(stateMachine.sendEvent(any(Event.class))).thenReturn(false);

        when(stateMachineAdapter.restore(caze)).thenReturn(stateMachine);

        caseService.performEvent(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, Event.CREATE);
    }

    @Test
    public void updateCases_ActionIsDelete_VerifyRemoveCaseInvoked()
        throws MethodArgumentNotValidException {
        caseEditDTO = new CaseEditDTO(1L, "desc", "name", 1, Status.NOT_RUN,
            Collections.emptyList(), Action.DELETE, "comment");
        caseEditDTO.setId(SIMPLE_CASE_ID);
        CaseDTO expectedCaseDTO = new CaseDTO(1L, "name", "desc", Collections.emptyList(),
            1, Collections.emptySet(), Status.NOT_RUN.getStatusName(), "comment", 1);
        List<CaseDTO> expectedCaseDTOs = Arrays.asList(expectedCaseDTO);

        CaseService mock = mock(CaseService.class);
        when(mock.removeCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, caseEditDTO.getId()))
            .thenReturn(expectedCaseDTO);
        Mockito.doCallRealMethod().when(mock).updateCases(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,
            Collections.singletonList(caseEditDTO));
        List<CaseDTO> actualUpdatedCaseDTOs = mock
            .updateCases(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, Collections.singletonList(caseEditDTO));

        assertEquals(expectedCaseDTOs, actualUpdatedCaseDTOs);
        verify(mock).removeCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(caseEditDTO.getId()));

    }

    @Test
    public void updateCases_ActionIsCreate_VerifyAddCaseInvoked()
        throws MethodArgumentNotValidException {
        caseEditDTO = new CaseEditDTO(1L, "desc", "name", 1,
            Status.NOT_RUN, Collections.emptyList(), Action.CREATE, "comment");
        caseEditDTO.setId(SIMPLE_CASE_ID);
        CaseDTO expectedCaseDTO = new CaseDTO(1L, "name", "desc", Collections.emptyList(),
            1, Collections.emptySet(), Status.NOT_RUN.getStatusName(), "comment", 1);
        List<CaseDTO> expectedCaseDTOs = Arrays.asList(expectedCaseDTO);

        CaseService mock = mock(CaseService.class);
        when(mock.addCaseToSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, caseEditDTO))
            .thenReturn(expectedCaseDTO);
        Mockito.doCallRealMethod().when(mock).updateCases(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,
            Collections.singletonList(caseEditDTO));

        List<CaseDTO> actualUpdatedCaseDTOs = mock
            .updateCases(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, Collections.singletonList(caseEditDTO));

        assertEquals(expectedCaseDTOs, actualUpdatedCaseDTOs);
        verify(mock).addCaseToSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(caseEditDTO));
    }
}