package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.entities.Action;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.transformers.CaseTransformer;
import com.epam.test_generator.transformers.StepTransformer;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.epam.test_generator.transformers.SuitTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

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

    @Before
    public void setUp() {
        step = new Step(SIMPLE_STEP_ID, 1, "Step desc", StepType.GIVEN);
        expectedStep = new StepDTO(SIMPLE_STEP_ID, 1, "Step desc", StepType.GIVEN);

        final List<Step> listSteps = new ArrayList<>();

        listSteps.add(new Step(1L, 1, "Step 1", StepType.GIVEN));
        listSteps.add(new Step(2L, 2, "Step 2", StepType.WHEN));
        listSteps.add(step);

        expectedListSteps.add(new StepDTO(1L, 1, "Step 1", StepType.GIVEN));
        expectedListSteps.add(new StepDTO(2L, 2, "Step 2", StepType.WHEN));
        expectedListSteps.add(expectedStep);

        caze = new Case(SIMPLE_CASE_ID, "name", "Case desc", listSteps, 1, setOfTags);

        final List<Case> listCases = new ArrayList<>();

        listCases.add(new Case(1L, "name 1", "Case 1", listSteps, 1, setOfTags));
        listCases.add(new Case(2L, "name 2", "Case 2", listSteps, 2, setOfTags));
        listCases.add(caze);

        suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", listCases, 1, setOfTags, 1);
    }

    @Test
    public void get_StepsByCaseId_Success(){
        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);

        when(stepTransformer.toDtoList(anyList())).thenReturn(expectedListSteps);

        List<StepDTO> actualStepList = stepService.getStepsByCaseId(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
        assertEquals(expectedListSteps, actualStepList);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID),eq(SIMPLE_CASE_ID));
        verify(stepTransformer).toDtoList(anyList());
    }

	@Test(expected = NotFoundException.class)
	public void get_StepsByCaseId_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(suitService).getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

		stepService.getStepsByCaseId(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
	}

	@Test(expected = NotFoundException.class)
	public void get_StepsByCaseId_NotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        doThrow(NotFoundException.class).when(caseService).getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID);

		stepService.getStepsByCaseId(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
	}

    @Test
    public void get_StepDTO_Valid() {
        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
        when(stepDAO.findOne(anyLong())).thenReturn(step);
        when(stepTransformer.toDto(any(Step.class))).thenReturn(expectedStep);

        StepDTO actualStep = stepService.getStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
        assertEquals(expectedStep, actualStep);

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID),eq(SIMPLE_CASE_ID));
        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepTransformer).toDto(any(Step.class));
    }

	@Test(expected = NotFoundException.class)
	public void get_Step_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(suitService).getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

		stepService.getStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void get_Step_NotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        doThrow(NotFoundException.class).when(caseService).getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID);

		stepService.getStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void get_Step_NotFoundExceptionFromStep() {
        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
		when(stepDAO.findOne(anyLong())).thenReturn(null);

		stepService.getStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

    @Test
    public void add_StepToCase_Success() {
        Step newStep = new Step(3L, 3, "Step 3", StepType.AND);
        StepDTO newStepDTO = new StepDTO(null, 3, "Step 3", StepType.AND);

        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
        when(stepDAO.save(any(Step.class))).thenReturn(newStep);
        when(stepTransformer.fromDto(any(StepDTO.class))).thenReturn(newStep);

        Long actualId = stepService.addStepToCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, newStepDTO);
        assertEquals(newStep.getId(), actualId);
        assertTrue(caze.getSteps().contains(newStep));

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID),eq(SIMPLE_CASE_ID));
        verify(stepDAO).save(any(Step.class));
        verify(stepTransformer).fromDto(any(StepDTO.class));
        verify(caseVersionDAO).save(eq(caze));
    }

	@Test(expected = NotFoundException.class)
	public void add_Step_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(suitService).getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

		stepService.addStepToCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new StepDTO());
	}

	@Test(expected = NotFoundException.class)
	public void add_Step_NotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        doThrow(NotFoundException.class).when(caseService).getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID);

		stepService.addStepToCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new StepDTO());
	}

    @Test
    public void update_Step_Success() {
        StepDTO updateStepDTO = new StepDTO(null, 3, "New Step desc", null);

        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
        when(stepDAO.findOne(anyLong())).thenReturn(step);

        stepService.updateStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, updateStepDTO);
        assertTrue(caze.getSteps().contains(step));

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID),eq(SIMPLE_CASE_ID));
        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepTransformer).mapDTOToEntity(any(StepDTO.class), eq(step));
        verify(stepDAO).save(eq(step));
        verify(caseVersionDAO).save(eq(caze));
    }

	@Test(expected = NotFoundException.class)
	public void update_Step_NotFoundExceptionFromSuit() {
        doThrow(NotFoundException.class).when(suitService).getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

		stepService.updateStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepDTO());
	}

	@Test(expected = NotFoundException.class)
	public void update_Step_NotFoundExceptionFromCase() {
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        doThrow(NotFoundException.class).when(caseService).getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID);

		stepService.updateStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepDTO());
	}

	@Test(expected = NotFoundException.class)
	public void update_Step_NotFoundExceptionFromStep() {
        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
		when(stepDAO.findOne(anyLong())).thenReturn(null);

		stepService.updateStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepDTO());
	}

    @Test
    public void remove_Step() {
        when(suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID)).thenReturn(suit);
        when(caseService.getCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,SIMPLE_CASE_ID)).thenReturn(caze);
        when(stepDAO.findOne(anyLong())).thenReturn(step);

        stepService.removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
        assertTrue(!caze.getSteps().contains(step));

        verify(suitService).getSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID),eq(SIMPLE_CASE_ID));
        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepDAO).delete(eq(SIMPLE_STEP_ID));
        verify(caseVersionDAO).save(eq(caze));
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
		when(stepDAO.findOne(anyLong())).thenReturn(null);

		stepService.removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

    @Test
    public void cascadeUpdate_Steps_Success() {
        StepDTO actionCreateDTO = new StepDTO(null, 1, "New Step 1", null);
        StepDTO actionDeleteDTO = new StepDTO(3L, 2, "New Step 2", null);
        StepDTO actionUpdateDTO = new StepDTO(4L, 3, "New Step 3", null);
        actionCreateDTO.setAction(Action.CREATE);
        actionDeleteDTO.setAction(Action.DELETE);
        actionUpdateDTO.setAction(Action.UPDATE);

        StepService mock = mock(StepService.class);
        Mockito.doCallRealMethod().when(mock).cascadeUpdateSteps(anyLong(), anyLong(), anyInt(), anyList());
        mock.cascadeUpdateSteps(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID,
            Lists.newArrayList(actionCreateDTO, actionDeleteDTO, actionUpdateDTO));

        verify(mock, times(1)).addStepToCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, actionCreateDTO);
        verify(mock, times(1))
            .updateStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, actionUpdateDTO.getId(), actionUpdateDTO);
        verify(mock, times(1)).removeStep(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, actionDeleteDTO.getId());
    }

    @Test
    public void cascadeUpdate_StepsWithoutActions_Success() {
        StepDTO actionCreateDTO = new StepDTO(null, 1, "New Step 1", null);
        StepDTO actionDeleteDTO = new StepDTO(3L, 2, "New Step 2", null);
        StepDTO actionUpdateDTO = new StepDTO(4L, 3, "New Step 3", null);

        StepService mock = mock(StepService.class);
        Mockito.doCallRealMethod().when(mock).cascadeUpdateSteps(anyLong(), anyLong(), anyInt(), anyList());
        mock.cascadeUpdateSteps(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID,
            Lists.newArrayList(actionCreateDTO, actionDeleteDTO, actionUpdateDTO));

        verify(mock, never()).addStepToCase(anyLong(), anyLong(), anyLong(), any(StepDTO.class));
        verify(mock, never()).updateStep(anyLong(), anyLong(), anyLong(), anyLong(), any(StepDTO.class));
        verify(mock, never()).removeStep(anyLong(), anyLong(), anyLong(), anyLong());
    }
}