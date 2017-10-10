package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.entities.*;
import com.epam.test_generator.transformers.StepTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StepServiceTest {

    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final long SIMPLE_STEP_ID = 3L;

    private Suit suit;
    private SuitDTO expectedSuit;

    private Case caze;
    private CaseDTO expetedCaze;

    private Step step;
    private StepDTO expectedStep;

    private List<Case> listCases;
    private List<CaseDTO> expectedListCases;

    private List<Step> listSteps;
    private List<StepDTO> expectedListSteps;
    private Set<Tag> setTags;
    private Set<TagDTO> expectedSetTags;

    @Mock
    private StepTransformer stepTransformer;

    @Mock
    private CaseDAO caseDAO;

    @Mock
    private StepDAO stepDAO;

    @InjectMocks
    private StepService stepService;

    @Before
    public void setUp() {
        listSteps = new ArrayList<>();
        listSteps.add(new Step(1L, 1, "Step 1", StepType.GIVEN));
        listSteps.add(new Step(2L, 2, "Step 2", StepType.WHEN));

        expectedListSteps = new ArrayList<>();
        expectedListSteps.add(new StepDTO(1L, 1, "Step 1", StepType.GIVEN.ordinal()));
        expectedListSteps.add(new StepDTO(2L, 2, "Step 2", StepType.WHEN.ordinal()));

        step = new Step(SIMPLE_STEP_ID, 1, "Step desc", StepType.GIVEN);
        expectedStep = new StepDTO(SIMPLE_STEP_ID, 1, "Step desc", StepType.GIVEN.ordinal());

        listCases = new ArrayList<>();
        listCases.add(new Case(1L, "Case 1", listSteps, 1, setTags));
        listCases.add(new Case(2L, "Case 2", listSteps, 2, setTags ));

        expectedListCases = new ArrayList<>();
        expectedListCases.add(new CaseDTO(1L, "Case 1", expectedListSteps, 1, expectedSetTags));
        expectedListCases.add(new CaseDTO(2L, "Case 2", expectedListSteps, 2, expectedSetTags));

        caze = new Case(SIMPLE_CASE_ID, "Case desc", listSteps, 1, setTags);
        expetedCaze = new CaseDTO(SIMPLE_CASE_ID, "Case desc", expectedListSteps, 1, expectedSetTags);

        suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", listCases, 1, "tag1");
        expectedSuit = new SuitDTO(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", expectedListCases, 1, "tag1");
    }

    @Test
    public void getStepsByCaseIdTest() throws Exception {
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepTransformer.toDtoList(anyList())).thenReturn(expectedListSteps);

        List<StepDTO> actualStepList = stepService.getStepsByCaseId(SIMPLE_CASE_ID);
        assertEquals(expectedListSteps, actualStepList);

        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepTransformer).toDtoList(anyList());
    }

    @Test
    public void getStepTest() throws Exception {
        when(stepDAO.findOne(anyLong())).thenReturn(step);
        when(stepTransformer.toDto(any(Step.class))).thenReturn(expectedStep);

        StepDTO actualStep = stepService.getStep(SIMPLE_STEP_ID);
        assertEquals(expectedStep, actualStep);

        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepTransformer).toDto(any(Step.class));
    }

    @Test
    public void addStepToCaseTest() throws Exception {
        Step newStep = new Step(3L, 3, "Step 3", StepType.AND);
        StepDTO newStepDTO = new StepDTO(null, 3, "Step 3", StepType.AND.ordinal());

        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepTransformer.fromDto(any(StepDTO.class))).thenReturn(newStep);
        when(stepDAO.save(any(Step.class))).thenReturn(newStep);

        Long actualId = stepService.addStepToCase(SIMPLE_CASE_ID, newStepDTO);
        assertEquals(newStep.getId(), actualId);

        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepTransformer).fromDto(any(StepDTO.class));
        verify(stepDAO).save(any(Step.class));
    }

    @Test
    public void updateStepTest() throws Exception {
        StepDTO updateStepDTO = new StepDTO(null,  3,"New Step desc", null);

        when(stepDAO.findOne(anyLong())).thenReturn(step);

        stepService.updateStep(SIMPLE_STEP_ID, updateStepDTO);

        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepTransformer).mapDTOToEntity(any(StepDTO.class), eq(step));
        verify(stepDAO).save(eq(step));
    }

    @Test
    public void removeStepTest() throws Exception {
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepDAO.findOne(anyLong())).thenReturn(step);

        stepService.removeStep(SIMPLE_CASE_ID, SIMPLE_STEP_ID);

        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepDAO).delete(eq(SIMPLE_STEP_ID));
    }

}