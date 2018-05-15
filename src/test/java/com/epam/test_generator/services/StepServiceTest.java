package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.step.StepTransformer;
import com.epam.test_generator.controllers.step.request.StepCreateDTO;
import com.epam.test_generator.controllers.step.request.StepUpdateDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.services.exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.bouncycastle.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StepServiceTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final long SIMPLE_STEP_ID = 3L;
    private final List<StepDTO> expectedListSteps = new ArrayList<>();
    private final Set<Tag> setOfTags = new HashSet<>();
    private Suit suit;

    private Case caze;

    private Step step;
    private StepDTO expectedStep;
    @Mock
    private StepTransformer stepTransformer;

    @Mock
    private StepDAO stepDAO;

    @InjectMocks
    private StepService stepService;

    @Mock
    private SuitService suitService;

    @Mock
    private CaseService caseService;

    @Mock
    private CaseVersionDAO caseVersionDAO;

    @Mock
    private SuitVersionDAO suitVersionDAO;

    @Before
    public void setUp() {
        step = new Step(SIMPLE_STEP_ID, 1, "Step desc", StepType.GIVEN, "Comment", Status.NOT_RUN);
        expectedStep = new StepDTO(SIMPLE_STEP_ID, 1, "Step desc", StepType.GIVEN, "Comment", Status.NOT_RUN.getStatusName());

        List<Step> listSteps = new ArrayList<>();

        listSteps.add(new Step(1L, 1, "Step 1", StepType.GIVEN, "Comment", Status.NOT_RUN));
        listSteps.add(new Step(2L, 2, "Step 2", StepType.WHEN, "Comment", Status.NOT_RUN));
        listSteps.add(step);

        expectedListSteps.add(new StepDTO(1L, 1, "Step 1", StepType.GIVEN, "Comment", Status.NOT_RUN.getStatusName()));
        expectedListSteps.add(new StepDTO(2L, 2, "Step 2", StepType.WHEN, "Comment", Status.NOT_RUN.getStatusName()));
        expectedListSteps.add(expectedStep);

        caze = new Case(SIMPLE_CASE_ID, "name", "Case desc", listSteps, 1, setOfTags, "comment");
        caze.setStatus(Status.NOT_RUN);

        List<Case> listCases = new ArrayList<>();

        listCases.add(
            new Case(1L, "name 1", "Case 1", new ArrayList<>(listSteps), 1, setOfTags, "comment1"));
        listCases.add(
            new Case(2L, "name 2", "Case 2", new ArrayList<>(listSteps), 2, setOfTags, "comment2"));
        listCases.add(caze);

        suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", listCases, 1, setOfTags, 1);
        suit.setStatus(Status.NOT_RUN);
    }

    @Test
    public void get_StepsByCaseId_Success(){
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);

        when(stepTransformer.toDtoList(anyList())).thenReturn(expectedListSteps);

        List<StepDTO> actualStepList = stepService.getStepsByCaseId(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
        assertEquals(expectedListSteps, actualStepList);

        verify(caseService).getCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID),eq(SIMPLE_CASE_ID));
        verify(stepTransformer).toDtoList(anyList());
    }

	@Test(expected = NotFoundException.class)
	public void get_StepsByCaseId_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(caseService)
            .getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);

		stepService.getStepsByCaseId(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
	}

	@Test(expected = NotFoundException.class)
	public void get_StepsByCaseId_NotFoundExceptionFromCase() {
        doThrow(NotFoundException.class).when(caseService).getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID);

		stepService.getStepsByCaseId(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
	}

    @Test
    public void get_StepDTO_Valid() {
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
        when(stepDAO.findById(anyLong())).thenReturn(Optional.of(step));
        when(stepTransformer.toDto(any(Step.class))).thenReturn(expectedStep);

        StepDTO actualStep = stepService.getStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
        assertEquals(expectedStep, actualStep);

        verify(caseService).getCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID),eq(SIMPLE_CASE_ID));
        verify(stepDAO).findById(eq(SIMPLE_STEP_ID));
        verify(stepTransformer).toDto(any(Step.class));
    }

	@Test(expected = NotFoundException.class)
	public void get_Step_NotFoundExceptionFromSuit() {

		stepService.getStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void get_Step_NotFoundExceptionFromCase() {
        doThrow(NotFoundException.class).when(caseService).getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID);

		stepService.getStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void get_Step_NotFoundExceptionFromStep() {
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
		when(stepDAO.findById(anyLong())).thenReturn(Optional.empty());

		stepService.getStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

    @Test
    public void add_StepToCase_Success() {
        Step newStep = new Step(3L, 3, "Step 3", StepType.AND, "Comment", Status.NOT_RUN);
        StepCreateDTO newStepDTO = new StepCreateDTO();
        newStepDTO.setDescription("Step 3");
        newStepDTO.setType(StepType.AND);
        newStepDTO.setComment("Comment");
        newStepDTO.setStatus(Status.NOT_RUN);

        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
        when(stepDAO.save(any(Step.class))).thenReturn(newStep);
        when(stepTransformer.fromDto(any(StepCreateDTO.class))).thenReturn(newStep);

        Long actualId = stepService.addStepToCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, newStepDTO);
        assertEquals(newStep.getId(), actualId);
        assertTrue(caze.getSteps().contains(newStep));

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID),eq(SIMPLE_CASE_ID));
        verify(stepDAO).save(any(Step.class));
        verify(stepTransformer).fromDto(any(StepCreateDTO.class));
        verify(caseVersionDAO).save(eq(caze));
    }

    @Test
    public void add_StepToCaseWithoutSteps_ChangesSuitAndStepStatusToNotRun() {
        Step newStep = new Step(3L, 3, "Step 3", StepType.AND, "Comment", Status.NOT_RUN);
        StepCreateDTO newStepDTO = new StepCreateDTO();
        newStepDTO.setDescription("Step 3");
        newStepDTO.setType(StepType.AND);
        newStepDTO.setComment("Comment");
        newStepDTO.setStatus(Status.NOT_RUN);

        Case newCase =
            new Case(3L, "name", "Case desc", new ArrayList<>(), 1, setOfTags, "comment");
        newCase.setStatus(Status.NOT_DONE);
        ArrayList<Case> cases = new ArrayList<>();
        cases.add(caze);
        Suit newSuit = new Suit(2L, "Suit 1", "Suit desc", cases, 1, setOfTags, 1);
        newSuit.setStatus(Status.NOT_DONE);

        when(suitService.getSuit(SIMPLE_PROJECT_ID, 2L)).thenReturn(newSuit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, 2L,3L)).thenReturn(newCase);
        when(stepDAO.save(any(Step.class))).thenReturn(newStep);
        when(stepTransformer.fromDto(any(StepCreateDTO.class))).thenReturn(newStep);

        Long actualId = stepService.addStepToCase(SIMPLE_PROJECT_ID, 2L, 3L, newStepDTO);
        assertEquals(Status.NOT_RUN, newCase.getStatus());
        assertEquals(Status.NOT_RUN, newSuit.getStatus());
        assertEquals(newStep.getId(), actualId);
        assertTrue(newCase.getSteps().contains(newStep));
    }

	@Test(expected = NotFoundException.class)
	public void add_Step_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(suitService).getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

		stepService.addStepToCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new StepCreateDTO());
	}

	@Test(expected = NotFoundException.class)
	public void add_Step_NotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        doThrow(NotFoundException.class).when(caseService).getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID);

		stepService.addStepToCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new StepCreateDTO());
	}

    @Test
    public void update_Step_Success() {
        StepUpdateDTO updateStepDTO = new StepUpdateDTO();
        updateStepDTO.setRowNumber(3);
        updateStepDTO.setDescription("New Step desc");
        updateStepDTO.setComment("Comment");
        updateStepDTO.setStatus(Status.NOT_RUN);

        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
        when(stepDAO.findById(anyLong())).thenReturn(Optional.of(step));
        when(stepTransformer.updateFromDto(eq(updateStepDTO), eq(step))).thenReturn(step);

        stepService.updateStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, updateStepDTO);
        assertTrue(caze.getSteps().contains(step));

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID),eq(SIMPLE_CASE_ID));
        verify(stepDAO).findById(eq(SIMPLE_STEP_ID));
        verify(stepDAO).save(eq(step));
        verify(caseVersionDAO).save(eq(caze));
    }

	@Test(expected = NotFoundException.class)
	public void update_Step_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(suitService).getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

		stepService.updateStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepUpdateDTO());
	}

	@Test(expected = NotFoundException.class)
	public void update_Step_NotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        doThrow(NotFoundException.class).when(caseService).getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID);

		stepService.updateStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepUpdateDTO());
	}

	@Test(expected = NotFoundException.class)
	public void update_Step_NotFoundExceptionFromStep() {
        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
		when(stepDAO.findById(anyLong())).thenReturn(Optional.empty());

		stepService.updateStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepUpdateDTO());
	}

    @Test
    public void remove_Step() {
        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
        when(stepDAO.findById(anyLong())).thenReturn(Optional.of(step));

        stepService.removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
        assertTrue(!caze.getSteps().contains(step));
        assertEquals(Status.NOT_RUN, caze.getStatus());
        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID),eq(SIMPLE_CASE_ID));
        verify(stepDAO).findById(eq(SIMPLE_STEP_ID));
        verify(stepDAO).delete(step);
        verify(caseVersionDAO).save(eq(caze));
    }

    @Test
    public void remove_AllStepsFromCase_CaseStatusChangesToNotDone() {
        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID))
            .thenReturn(caze);
        when(stepDAO.findById(anyLong())).thenReturn(Optional.of(caze.getSteps().get(0)))
                                        .thenReturn(Optional.of(caze.getSteps().get(1)))
                                        .thenReturn(Optional.of(step));
        stepService.removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
        stepService.removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
        stepService.removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
        assertEquals(Status.NOT_DONE, caze.getStatus());
        assertEquals(Status.NOT_DONE, suit.getStatus());
    }

    @Test
    public void remove_AllStepsFromAllCases_SuitStatusChangesToNotDone() {
        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        for(Case caze : suit.getCases()) {
            when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID))
                .thenReturn(caze);
            when(stepDAO.findById(anyLong())).thenReturn(Optional.of(caze.getSteps().get(0)))
                                            .thenReturn(Optional.of(caze.getSteps().get(1)))
                                            .thenReturn(Optional.of(caze.getSteps().get(2)));
            stepService
                .removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
            stepService
                .removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
            stepService
                .removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);

        }
        assertEquals(Status.NOT_DONE, suit.getStatus());
    }

	@Test(expected = NotFoundException.class)
	public void remove_Step_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(suitService).getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

		stepService.removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void remove_Step_NotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        doThrow(NotFoundException.class).when(caseService).getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID);

		stepService.removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void remove_Step_NotFoundExceptionFromStep() {
        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
		when(stepDAO.findById(anyLong())).thenReturn(Optional.empty());

		stepService.removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}
}