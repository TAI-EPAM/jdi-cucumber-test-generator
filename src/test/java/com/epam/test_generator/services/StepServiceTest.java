package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertTrue;


import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.entities.*;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.transformers.StepTransformer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StepServiceTest {

    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final long SIMPLE_STEP_ID = 3L;

    private Suit suit;

    private Case caze;

    private Step step;
    private StepDTO expectedStep;

    private List<StepDTO> expectedListSteps = new ArrayList<>();
    private Set<Tag> setTags = new HashSet<>();

    @Mock
    private StepTransformer stepTransformer;

    @Mock
    private SuitDAO suitDAO;

    @Mock
    private CaseDAO caseDAO;

    @Mock
    private StepDAO stepDAO;

    @Mock
    private CaseVersionDAO caseVersionDAO;

    @InjectMocks
    private StepService stepService;

    @Before
    public void setUp() {
        step = new Step(SIMPLE_STEP_ID, 1, "Step desc", StepType.GIVEN);
        expectedStep = new StepDTO(SIMPLE_STEP_ID, 1, "Step desc", StepType.GIVEN);

        List<Step> listSteps = new ArrayList<>();

        listSteps.add(new Step(1L, 1, "Step 1", StepType.GIVEN));
        listSteps.add(new Step(2L, 2, "Step 2", StepType.WHEN));
        listSteps.add(step);

        expectedListSteps.add(new StepDTO(1L, 1, "Step 1", StepType.GIVEN));
        expectedListSteps.add(new StepDTO(2L, 2, "Step 2", StepType.WHEN));
        expectedListSteps.add(expectedStep);

        caze = new Case(SIMPLE_CASE_ID, "Case desc", listSteps, 1, setTags);

        List<Case> listCases = new ArrayList<>();

        listCases.add(new Case(1L, "Case 1", listSteps, 1, setTags));
        listCases.add(new Case(2L, "Case 2", listSteps, 2, setTags));
        listCases.add(caze);

        suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", listCases, 1, "tag1");
    }

    @Test
    public void getStepsByCaseIdTest() throws Exception {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepTransformer.toDtoList(anyList())).thenReturn(expectedListSteps);

        List<StepDTO> actualStepList = stepService.getStepsByCaseId(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
        assertEquals(expectedListSteps, actualStepList);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepTransformer).toDtoList(anyList());
    }

	@Test(expected = NotFoundException.class)
	public void getStepsByCaseIdTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		stepService.getStepsByCaseId(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
	}

	@Test(expected = NotFoundException.class)
	public void getStepsByCaseIdTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		stepService.getStepsByCaseId(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
	}

    @Test
    public void getStepTest() throws Exception {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepDAO.findOne(anyLong())).thenReturn(step);
        when(stepTransformer.toDto(any(Step.class))).thenReturn(expectedStep);

        StepDTO actualStep = stepService.getStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
        assertEquals(expectedStep, actualStep);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepTransformer).toDto(any(Step.class));
    }

	@Test(expected = NotFoundException.class)
	public void getStepTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		stepService.getStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void getStepTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		stepService.getStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void getStepTest_expectNotFoundExceptionFromStep() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(caze);
		when(stepDAO.findOne(anyLong())).thenReturn(null);

		stepService.getStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

    @Test
    public void addStepToCaseTest() throws Exception {
        Step newStep = new Step(3L, 3, "Step 3", StepType.AND);
        StepDTO newStepDTO = new StepDTO(null, 3, "Step 3", StepType.AND);

        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepDAO.save(any(Step.class))).thenReturn(newStep);
        when(stepTransformer.fromDto(any(StepDTO.class))).thenReturn(newStep);

        Long actualId = stepService.addStepToCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, newStepDTO);
        assertEquals(newStep.getId(), actualId);
        assertTrue(caze.getSteps().contains(newStep));

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepDAO).save(any(Step.class));
        verify(stepTransformer).fromDto(any(StepDTO.class));
        verify(caseVersionDAO).save(eq(caze));
    }

	@Test(expected = NotFoundException.class)
	public void addStepTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		stepService.addStepToCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new StepDTO());
	}

	@Test(expected = NotFoundException.class)
	public void addStepTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		stepService.addStepToCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new StepDTO());
	}

    @Test
    public void updateStepTest() throws Exception {
        StepDTO updateStepDTO = new StepDTO(null, 3, "New Step desc", null);

        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepDAO.findOne(anyLong())).thenReturn(step);

        stepService.updateStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, updateStepDTO);
        assertTrue(caze.getSteps().contains(step));

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepTransformer).mapDTOToEntity(any(StepDTO.class), eq(step));
        verify(stepDAO).save(eq(step));
        verify(caseVersionDAO).save(eq(caze));
    }

	@Test(expected = NotFoundException.class)
	public void updateStepTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		stepService.updateStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepDTO());
	}

	@Test(expected = NotFoundException.class)
	public void updateStepTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		stepService.updateStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepDTO());
	}

	@Test(expected = NotFoundException.class)
	public void updateStepTest_expectNotFoundExceptionFromStep() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(caze);
		when(stepDAO.findOne(anyLong())).thenReturn(null);

		stepService.updateStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepDTO());
	}

    @Test
    public void removeStepTest() throws Exception {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepDAO.findOne(anyLong())).thenReturn(step);

        stepService.removeStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
        assertTrue(!caze.getSteps().contains(step));

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepDAO).delete(eq(SIMPLE_STEP_ID));
        verify(caseVersionDAO).save(eq(caze));
    }

	@Test(expected = NotFoundException.class)
	public void removeStepTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		stepService.removeStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void removeStepTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		stepService.removeStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void removeStepTest_expectNotFoundExceptionFromStep() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(caze);
		when(stepDAO.findOne(anyLong())).thenReturn(null);

		stepService.removeStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

    @Test
    public void cascadeUpdateSteps() {
        StepDTO actionCreateDTO = new StepDTO(null, 1, "New Step 1", null);
        StepDTO actionDeleteDTO = new StepDTO(3L, 2, "New Step 2", null);
        StepDTO actionUpdateDTO  = new StepDTO(4L, 3, "New Step 3", null);
        actionCreateDTO.setAction(Action.CREATE);
        actionDeleteDTO.setAction(Action.DELETE);
        actionUpdateDTO.setAction(Action.UPDATE);

        StepService mock = mock(StepService.class);
        Mockito.doCallRealMethod().when(mock).cascadeUpdateSteps(anyLong(),anyInt(),anyList());
        mock.cascadeUpdateSteps(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, Lists.newArrayList(actionCreateDTO, actionDeleteDTO, actionUpdateDTO));

        verify(mock , times(1)).addStepToCase(SIMPLE_SUIT_ID,SIMPLE_CASE_ID, actionCreateDTO);
        verify(mock, times(1)).updateStep(SIMPLE_SUIT_ID,SIMPLE_CASE_ID, actionUpdateDTO.getId(),actionUpdateDTO);
        verify(mock, times(1)).removeStep(SIMPLE_SUIT_ID,SIMPLE_CASE_ID, actionDeleteDTO.getId());
    }

    @Test
    public void cascadeUpdateSteps_WithoutActions() {
        StepDTO actionCreateDTO = new StepDTO(null, 1, "New Step 1", null);
        StepDTO actionDeleteDTO = new StepDTO(3L, 2, "New Step 2", null);
        StepDTO actionUpdateDTO  = new StepDTO(4L, 3, "New Step 3", null);

        StepService mock = mock(StepService.class);
        Mockito.doCallRealMethod().when(mock).cascadeUpdateSteps(anyLong(),anyInt(),anyList());
        mock.cascadeUpdateSteps(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, Lists.newArrayList(actionCreateDTO, actionDeleteDTO, actionUpdateDTO));

        verify(mock, never()).addStepToCase(anyLong(),anyLong(), any(StepDTO.class));
        verify(mock, never()).updateStep(anyLong(),anyLong(),anyLong(), any(StepDTO.class));
        verify(mock, never()).removeStep(anyLong(),anyLong(), anyLong());
    }
}