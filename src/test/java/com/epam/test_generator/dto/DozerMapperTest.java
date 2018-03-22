package com.epam.test_generator.dto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.entities.Suit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DozerMapperTest {

    private DozerMapper dozerMapper;

    @Before
    public void setUp() {
        dozerMapper = new DozerMapper();
    }

    @Test
    public void map_SuitToSuitDTO_Valid() {
        Suit suit = new Suit();
        SuitDTO suitDTO = new SuitDTO();

        suit.setId(1L);
        suit.setName("Suit name");
        suit.setDescription("Suit description");
        suit.setPriority(1);
        suit.setRowNumber(1);


        dozerMapper.map(suit, suitDTO);

        assertThat(suit.getId(), is(equalTo(suitDTO.getId())));
        assertThat(suit.getName(), is(equalTo(suitDTO.getName())));
        assertThat(suit.getDescription(), is(equalTo(suitDTO.getDescription())));
        assertThat(suit.getPriority(), is(equalTo(suitDTO.getPriority())));
    }

    @Test
    public void map_CaseToCaseDTO_Valid() {
        final Case caze = new Case();
        final CaseDTO caseDTO = new CaseDTO();

        caze.setId(1L);
        caze.setName("Case name");
        caze.setDescription("Case description");
        caze.setPriority(1);

        dozerMapper.map(caze, caseDTO);

        assertThat(caze.getId(), is(equalTo(caseDTO.getId())));
        assertThat(caze.getName(), is(equalTo(caseDTO.getName())));
        assertThat(caze.getDescription(), is(equalTo(caseDTO.getDescription())));
        assertThat(caze.getPriority(), is(equalTo(caseDTO.getPriority())));
    }

    @Test
    public void map_StepToStepDTO_Valid() {
        Step step = new Step();
        StepDTO stepDTO = new StepDTO();

        step.setId(1L);
        step.setType(StepType.GIVEN);
        step.setDescription("Case description");
        step.setRowNumber(1);

        dozerMapper.map(step, stepDTO);

        assertThat(step.getId(), is(equalTo(stepDTO.getId())));
        assertThat(StepType.GIVEN, is(equalTo(stepDTO.getType())));
        assertThat(step.getDescription(), is(equalTo(stepDTO.getDescription())));
        assertThat(step.getRowNumber(), is(equalTo(stepDTO.getRowNumber())));
    }

    @Test
    public void map_StepSuggestionToStepSuggestionDTO_Valid() {
        StepSuggestion stepSuggestion = new StepSuggestion();
        StepSuggestionDTO stepSuggestionDTO = new StepSuggestionDTO();

        stepSuggestion.setId(1L);
        stepSuggestion.setType(StepType.GIVEN);
        stepSuggestion.setContent("Step suggestion description");
        stepSuggestion.setVersion(0L);

        dozerMapper.map(stepSuggestion, stepSuggestionDTO);

        assertThat(stepSuggestion.getId(), is(equalTo(stepSuggestionDTO.getId())));
        assertThat(StepType.GIVEN, is(equalTo(stepSuggestionDTO.getType())));
        assertThat(stepSuggestion.getContent(), is(equalTo(stepSuggestionDTO.getContent())));
        assertThat(stepSuggestion.getVersion(), is(equalTo(stepSuggestionDTO.getVersion())));
    }
}