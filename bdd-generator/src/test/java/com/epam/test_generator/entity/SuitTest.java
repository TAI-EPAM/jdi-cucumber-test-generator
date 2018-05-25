package com.epam.test_generator.entity;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.entities.api.SuitTrait;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class SuitTest {

    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final long SIMPLE_STEP_ID = 3L;
    private SuitTrait suit;

    private Case caze;

    @Before
    public void setUp() {
        Step step = new Step(SIMPLE_STEP_ID, 1, "Step desc", StepType.GIVEN, "Comment",
            Status.NOT_RUN);

        List<Step> steps = new ArrayList<>();

        steps.add(new Step(1L, 1, "Step 1", StepType.GIVEN, "Comment", Status.NOT_RUN));
        steps.add(new Step(2L, 2, "Step 2", StepType.WHEN, "Comment", Status.NOT_RUN));
        steps.add(step);

        Set<Tag> setOfTags = new HashSet<>();

        caze = new Case(SIMPLE_CASE_ID, "name", "Case desc", steps, 1, setOfTags, "comment");
        caze.setStatus(Status.NOT_RUN);

        List<Case> listCases = new ArrayList<>();

        listCases.add(
            new Case(1L, "name 1", "Case 1", new ArrayList<>(), 1, setOfTags, "comment1"));
        listCases.add(
            new Case(2L, "name 2", "Case 2", new ArrayList<>(), 2, setOfTags, "comment2"));
        listCases.add(caze);

        suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", listCases, 1, setOfTags, 1);
    }
    @Test
    public void hasStepsInAllCases_SuitWithSomeCasesWithoutSteps_False(){
        assertFalse(suit.hasStepsInAllCases());
    }

    @Test
    public void remove_SingleCaseWithStepsFromSuit_ChangesSuitStatusToNotDone(){
        suit.removeCase(caze);
        assertTrue(Status.NOT_DONE.equals(suit.getStatus()));
    }


}
