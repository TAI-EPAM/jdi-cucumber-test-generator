package com.epam.test_generator.services.utils;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.services.exceptions.ProjectClosedException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UtilsService {

    public static<T> void checkNotNull(T obj) {
        if (obj == null)
            throw new NotFoundException();
    }

    public static void checkProjectIsActive(Project project) {
        if (!project.isActive()) {
            throw new ProjectClosedException(
                "project with id=" + project.getId() + " is closed (readonly)",
                project.getId());
        }
    }

    public static void userBelongsToProject(Project project, User user) {
        Set<User> userSet = project.getUsers();

        if (userSet == null || userSet.stream()
            .noneMatch(u -> Objects.equals(user.getId(), u.getId()))) {
            throw new BadRequestException("Error: user does not access to project " + project.getName());
        }
    }
    public static void suitBelongsToProject(Project project, Suit suit) {
        List<Suit> suitList = project.getSuits();

        if (suitList == null || suitList.stream()
            .noneMatch(s -> Objects.equals(s.getId(), suit.getId()))) {
            throw new BadRequestException("Error: project " + project.getName() + " does not have suit " + suit.getName());
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
