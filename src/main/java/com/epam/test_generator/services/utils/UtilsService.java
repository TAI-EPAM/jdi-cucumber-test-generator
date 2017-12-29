package com.epam.test_generator.services.utils;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UtilsService {

    public static void checkNotNull(Suit suit) {
        if (suit == null) {
            throw new NotFoundException();
        }
    }

    public static void checkNotNull(Case caze) {
        if (caze == null) {
            throw new NotFoundException();
        }
    }

    public static void checkNotNull(Step step) {
        if (step == null) {
            throw new NotFoundException();
        }
    }

    public static void checkNotNull(Tag tag) {
        if (tag == null) {
            throw new NotFoundException();
        }
    }

    public static void checkNotNull(StepSuggestion stepSuggestion) {
        if (stepSuggestion == null) {
            throw new NotFoundException();
        }
    }


    public static void caseBelongsToSuit(Case caze, Suit suit) {
        List<Case> caseList = suit.getCases();

        if (caseList == null || caseList.stream()
            .noneMatch(c -> Objects.equals(c.getId(), caze.getId()))) {
            throw new BadRequestException();
        }
    }

    public static void stepBelongsToCase(Step step, Case caze) {
        List<Step> stepList = caze.getSteps();

        if (stepList == null || stepList.stream()
            .noneMatch(s -> Objects.equals(s.getId(), step.getId()))) {
            throw new BadRequestException();
        }
    }

    public static void tagBelongsToCase(Tag tag, Case caze) {
        Set<Tag> tagSet = caze.getTags();

        if (tagSet == null || tagSet.stream()
            .noneMatch(t -> Objects.equals(t.getId(), tag.getId()))) {
            throw new BadRequestException();
        }

    }
}
