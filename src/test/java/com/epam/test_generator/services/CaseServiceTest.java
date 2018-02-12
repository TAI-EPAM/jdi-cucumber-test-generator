package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.CaseVersionDTO;
import com.epam.test_generator.dto.EditCaseDTO;
import com.epam.test_generator.dto.PropertyDifferenceDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.TagDTO;
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
import com.epam.test_generator.transformers.CaseTransformer;
import com.epam.test_generator.transformers.CaseVersionTransformer;
import com.epam.test_generator.transformers.SuitTransformer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RunWith(MockitoJUnitRunner.class)
public class CaseServiceTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final String SIMPLE_COMMIT_ID = "1.5";

    private Suit suit;
    private SuitDTO suitDTO;

    private Case caze;
    private CaseDTO expectedCaseDTO;
    private Case caseToRestore;

    private List<StepDTO> listStepDtos = new ArrayList<>();
    private EditCaseDTO editCaseDTO;

    private List<CaseVersion> caseVersions;
    private List<CaseVersionDTO> expectedCaseVersions;
    private List<Step> listSteps = new ArrayList<>();
    private List<StepDTO> expectedListSteps = new ArrayList<>();
    private Set<Tag> setTags = new HashSet<>();
    private Set<TagDTO> tagDTOS = new HashSet<>();
    private Set<Tag> setOfTags = new HashSet<>();
    private Set<TagDTO> expectedSetTags = new HashSet<>();

    @Mock
    private CaseDAO caseDAO;

    @Mock
    private CaseVersionDAO caseVersionDAO;

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
    private CascadeUpdateService cascadeUpdateService;

    @Mock
    private StateMachine<Status, Event> stateMachine;

    @Before
    public void setUp() {
        final List<Case> listCases = new ArrayList<>();

        listCases.add(new Case(1L, "name 1", "Case 1",
                listSteps, 1, setOfTags, "comment 1"));
        listCases.add(new Case(2L, "name 2", "Case 2",
                listSteps, 2, setOfTags, "comment 2"));

        caze = new Case(SIMPLE_CASE_ID, "Case name", "Case desc",
                listSteps, 1, setOfTags, "comment");
        expectedCaseDTO = new CaseDTO(SIMPLE_CASE_ID, "Case name", "Case desc",
                expectedListSteps, 1, expectedSetTags, Status.NOT_DONE, "comment");
        suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc",
                listCases, 1, setOfTags, 1);
        caze = new Case(SIMPLE_CASE_ID, "Case name", "Case desc", listSteps, 1, setOfTags, "comment");
        expectedCaseDTO = new CaseDTO(SIMPLE_CASE_ID, "Case name", "Case desc", expectedListSteps, 1,
                expectedSetTags, Status.NOT_DONE,  "comment");
        caseToRestore = new Case(SIMPLE_CASE_ID, "new name", "new description",
                Lists.newArrayList(), 3, Sets.newHashSet(),  "comment");
        suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", listCases, 1, setOfTags, 1);

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
    public void get_Case_Success(){
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);

        final Case actualCase = caseService.getCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
        assertEquals(caze, actualCase);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void get_CaseDTO_Success(){
        when(caseTransformer.toDto(any())).thenReturn(expectedCaseDTO);
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);

        final CaseDTO actualCaseDTO = caseService.getCaseDTO(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
        assertEquals(expectedCaseDTO, actualCaseDTO);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
    }

    @Test(expected = NotFoundException.class)
    public void get_Case_expectNotFoundExceptionFromSuit() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(null);

        caseService.getCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test(expected = NotFoundException.class)
    public void get_Case_expectNotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(null);

        caseService.getCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test
    public void add_CaseToSuit_Success()  {
        final Case newCase = new Case(3L, "Case name", "Case 3", listSteps, 2, setOfTags,  "comment");
        final CaseDTO newCaseDTO = new CaseDTO(null, "Case name", "Case 3", expectedListSteps, 2,
            expectedSetTags, Status.NOT_DONE,  "comment");

        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseTransformer.fromDto(any(CaseDTO.class))).thenReturn(newCase);
        when(caseDAO.save(any(Case.class))).thenReturn(newCase);

        final Long actualId = caseService.addCaseToSuit(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, newCaseDTO);
        assertEquals(newCase.getId(), actualId);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID));
        verify(caseTransformer).fromDto(any(CaseDTO.class));
        verify(caseDAO).save(any(Case.class));
        verify(caseVersionDAO).save(eq(newCase));
    }

    @Test(expected = NotFoundException.class)
    public void add_CaseToSuit_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(suitService).getSuit(anyLong(), anyLong());
        when(suitTransformer.fromDto(any(SuitDTO.class))).thenReturn(null);
        caseService.addCaseToSuit(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, new CaseDTO());
    }

    @Test
    public void update_Case_Success(){
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);

        editCaseDTO = new EditCaseDTO(1L,caze.getDescription(), caze.getName(), caze.getPriority(),
                caze.getStatus(), Collections.emptyList(), Action.UPDATE, caze.getComment());

        caseService.updateCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID, editCaseDTO);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(cascadeUpdateService).cascadeCaseStepsUpdate(SIMPLE_PROJECT_ID,SIMPLE_SUIT_ID,SIMPLE_CASE_ID,editCaseDTO);
        verify(caseVersionDAO).save(eq(caze));
    }

    @Test(expected = NotFoundException.class)
    public void update_Case_NotFoundExceptionFromSuit() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(null);

        caseService.updateCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new EditCaseDTO());
    }

    @Test(expected = NotFoundException.class)
    public void update_Case_NotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(null);

        caseService.updateCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new EditCaseDTO());
    }

    @Test
    public void remove_Case_Success(){
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        doNothing().when(caseDAO).delete(caze);

        caseService.removeCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID));
        verify(caseDAO).delete(eq(SIMPLE_CASE_ID));
        verify(caseVersionDAO).delete(eq(caze));
    }

    @Test(expected = NotFoundException.class)
    public void remove_Case_NotFoundExceptionFromSuit() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(null);

        caseService.removeCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test(expected = NotFoundException.class)
    public void remove_Case_NotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(null);

        caseService.removeCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test
    public void remove_Cases_Success(){
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        List<Long> deleteCaseIds = Arrays.asList(1L, 2L);

        caseService.removeCases(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, deleteCaseIds);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID));
        verify(caseDAO).delete(eq(1L));
        verify(caseDAO).delete(eq(2L));
        verify(caseVersionDAO, times(2)).delete(any(Case.class));
    }

    @Test(expected = NotFoundException.class)
    public void remove_Cases_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(suitService).getSuit(anyLong(), anyLong());
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(null);

        List<Long> caseIds = new ArrayList<>();
        caseIds.add(SIMPLE_CASE_ID);
        caseService.removeCases(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, caseIds);
    }

    @Test
    public void getCaseVersions_SimpleCase_ReturnExpectedCaseVersionDTOs() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(caseVersionDAO.findAll(anyLong())).thenReturn(caseVersions);
        when(caseVersionTransformer.toDtoList(anyList())).thenReturn(expectedCaseVersions);

        List<CaseVersionDTO> caseVersionDTOs = caseService
            .getCaseVersions(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID);

        assertEquals(expectedCaseVersions, caseVersionDTOs);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(SIMPLE_CASE_ID);
        verify(caseVersionDAO).findAll(SIMPLE_CASE_ID);
        verify(caseVersionTransformer).toDtoList(eq(caseVersions));
    }

    @Test(expected = NotFoundException.class)
    public void getCaseVersions_NullSuit_NotFoundException() {
        doThrow(NotFoundException.class).when(suitService).getSuit(anyLong(), anyLong());

        caseService.getCaseVersions(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getCaseVersions_NullCase_NotFoundException() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(null);

        caseService.getCaseVersions(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test(expected = BadRequestException.class)
    public void getCaseVersions_CaseDoesNotBelongsToSuit_BadRequestException() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(new Case());

        caseService.getCaseVersions(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test
    public void restoreCase_SimpleCase_Restored() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(caseVersionDAO.findByCommitId(anyLong(), anyString())).thenReturn(caseToRestore);

        caseService.restoreCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(SIMPLE_CASE_ID);
        verify(caseVersionDAO).findByCommitId(SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
        verify(caseDAO).save(eq(caseToRestore));
        verify(caseVersionDAO).save(eq(caseToRestore));
    }

    @Test(expected = NotFoundException.class)
    public void restoreCase_NullSuit_NotFoundException() {
        doThrow(NotFoundException.class).when(suitService).getSuit(anyLong(), anyLong());

        caseService.restoreCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void restoreCaseByCaseVersionId_NullCase_NotFoundException() {
        when(caseDAO.findOne(anyLong())).thenReturn(null);

        caseService.restoreCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
    }

    @Test(expected = BadRequestException.class)
    public void restoreCase_CaseDoesNotBelongsToSuit_BadRequestException() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(new Case());

        caseService.restoreCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void restoreCase_NullCaseToRestore_NotFoundException() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(caseVersionDAO.findByCommitId(anyLong(), anyString())).thenReturn(null);

        caseService.restoreCase(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
    }

    @Test
    public void performEvent_StatusFromNotDoneToNotRun_Valid() throws Exception {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stateMachine.sendEvent(any(Event.class))).thenReturn(true);

        when(stateMachineAdapter.restore(caze)).thenReturn(stateMachine);

        caze.setStatus(Status.NOT_DONE);

        caseService.performEvent(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID, Event.CREATE);

        verify(stateMachineAdapter).persist(eq(stateMachine), eq(caze));
        verify(caseVersionDAO).save(eq(caze));
    }

    @Test(expected = BadRequestException.class)
    public void perform_Event_BadRequestException() throws Exception {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stateMachine.sendEvent(any(Event.class))).thenReturn(false);

        when(stateMachineAdapter.restore(caze)).thenReturn(stateMachine);

        caseService.performEvent(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, SIMPLE_CASE_ID, Event.CREATE);
    }

    @Test
    public void updateCases_ActionIsDelete_VerifyRemoveCaseInvoked()
        throws MethodArgumentNotValidException {
        editCaseDTO = new EditCaseDTO(1l,"desc", "name", 1, Status.NOT_RUN, Collections.emptyList(), Action.DELETE,  "comment");
        editCaseDTO.setId(SIMPLE_CASE_ID);

        CaseService mock = mock(CaseService.class);
        Mockito.doCallRealMethod().when(mock).updateCases(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,
            Collections.singletonList(editCaseDTO));

        mock.updateCases(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, Collections.singletonList(editCaseDTO));
        verify(mock).removeCase(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID), eq(editCaseDTO.getId()));
    }

    @Test
    public void updateCases_ActionIsUpdate_VerifyUpdateCaseInvoked()
        throws MethodArgumentNotValidException {
        editCaseDTO = new EditCaseDTO(1l,"desc", "name", 1,
                Status.NOT_RUN, Collections.emptyList(), Action.UPDATE,  "comment");
        editCaseDTO.setId(SIMPLE_CASE_ID);

        CaseService mock = mock(CaseService.class);
        Mockito.doCallRealMethod().when(mock).updateCases(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,
            Collections.singletonList(editCaseDTO));

        mock.updateCases(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, Collections.singletonList(editCaseDTO));
        verify(mock).updateCase(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(editCaseDTO));
    }

    @Test
    public void updateCases_ActionIsCreate_VerifyAddCaseInvoked()
        throws MethodArgumentNotValidException {
        editCaseDTO = new EditCaseDTO(1l, "desc", "name", 1,
                Status.NOT_RUN, Collections.emptyList(), Action.CREATE,  "comment");
        editCaseDTO.setId(SIMPLE_CASE_ID);

        CaseService mock = mock(CaseService.class);
        Mockito.doCallRealMethod().when(mock).updateCases(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,
            Collections.singletonList(editCaseDTO));

        mock.updateCases(SIMPLE_PROJECT_ID , SIMPLE_SUIT_ID, Collections.singletonList(editCaseDTO));
        verify(mock).addCaseToSuit(eq(SIMPLE_PROJECT_ID) , eq(SIMPLE_SUIT_ID), eq(editCaseDTO));
    }
}