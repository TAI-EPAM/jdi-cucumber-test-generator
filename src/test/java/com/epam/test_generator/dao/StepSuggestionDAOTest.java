package com.epam.test_generator.dao;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.StepSuggestionDAO;
import com.epam.test_generator.entities.StepSuggestion;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={DatabaseConfigForTests.class})
@Transactional
public class StepSuggestionDAOTest {

    @Autowired
    StepSuggestionDAO stepSuggestionDAO;

    @Test
    public void testCreateAndRetrieve() {
        StepSuggestion originalStepSuggestion = new StepSuggestion();
        originalStepSuggestion.setContent("content");
        originalStepSuggestion.setType(2);

        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        StepSuggestion newStepSuggestion = new StepSuggestion();
        newStepSuggestion.setId(id);
        newStepSuggestion.setContent("content");
        newStepSuggestion.setType(2);

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findOne(id));
    }

    @Test
    public void testUpdateContent() {
        StepSuggestion originalStepSuggestion = new StepSuggestion();
        originalStepSuggestion.setContent("content");
        originalStepSuggestion.setType(2);

        stepSuggestionDAO.save(originalStepSuggestion);
        originalStepSuggestion.setContent("new content");
        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        StepSuggestion newStepSuggestion = new StepSuggestion();
        newStepSuggestion.setId(id);
        newStepSuggestion.setContent("new content");
        newStepSuggestion.setType(2);

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findOne(id));
    }

    @Test
    public void testUpdateType() {
        StepSuggestion originalStepSuggestion = new StepSuggestion();
        originalStepSuggestion.setContent("content");
        originalStepSuggestion.setType(2);

        stepSuggestionDAO.save(originalStepSuggestion);
        originalStepSuggestion.setType(3);
        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        StepSuggestion newStepSuggestion = new StepSuggestion();
        newStepSuggestion.setId(id);
        newStepSuggestion.setContent("content");
        newStepSuggestion.setType(3);

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findOne(id));
    }

    @Test
    public void testRemoveById() {
        StepSuggestion originalStepSuggestion = new StepSuggestion();
        originalStepSuggestion.setContent("content");
        originalStepSuggestion.setType(2);

        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        stepSuggestionDAO.delete(id);

        Assert.assertTrue(!stepSuggestionDAO.exists(id));
    }

    @Test
    public void testRemove() {
        StepSuggestion originalStepSuggestion = new StepSuggestion();
        originalStepSuggestion.setContent("content");
        originalStepSuggestion.setType(2);

        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        stepSuggestionDAO.delete(originalStepSuggestion);

        Assert.assertTrue(!stepSuggestionDAO.exists(id));
    }

    @Test
    public void testAddList() {
        StepSuggestion stepSuggestion1 = new StepSuggestion();
        stepSuggestion1.setContent("content1");
        stepSuggestion1.setType(2);

        StepSuggestion stepSuggestion2 = new StepSuggestion();
        stepSuggestion2.setContent("content2");
        stepSuggestion2.setType(1);;

        StepSuggestion stepSuggestion3 = new StepSuggestion();
        stepSuggestion3.setContent("content3");
        stepSuggestion3.setType(2);

        ArrayList<StepSuggestion> steps = new ArrayList<>();
        steps.add(stepSuggestion1);
        steps.add(stepSuggestion2);
        steps.add(stepSuggestion3);

        List<Long> ids = stepSuggestionDAO.save(steps).stream().map(StepSuggestion::getId).collect(Collectors.toList());

        StepSuggestion stepSuggestion4 = new StepSuggestion();
        stepSuggestion4.setId(ids.get(0));
        stepSuggestion4.setContent("content1");
        stepSuggestion4.setType(2);

        StepSuggestion stepSuggestion5 = new StepSuggestion();
        stepSuggestion5.setId(ids.get(1));
        stepSuggestion5.setContent("content2");
        stepSuggestion5.setType(1);;

        StepSuggestion stepSuggestion6 = new StepSuggestion();
        stepSuggestion6.setId(ids.get(2));
        stepSuggestion6.setContent("content3");
        stepSuggestion6.setType(2);

        ArrayList<StepSuggestion> newStepSuggestions = new ArrayList<>();
        newStepSuggestions.add(stepSuggestion4);
        newStepSuggestions.add(stepSuggestion5);
        newStepSuggestions.add(stepSuggestion6);

        Assert.assertTrue(newStepSuggestions.equals(stepSuggestionDAO.findAll()));
    }

    @Test
    public void testRemoveList() {
        StepSuggestion stepSuggestion1 = new StepSuggestion();
        stepSuggestion1.setContent("content1");
        stepSuggestion1.setType(2);

        StepSuggestion stepSuggestion2 = new StepSuggestion();
        stepSuggestion2.setContent("content2");
        stepSuggestion2.setType(1);;

        StepSuggestion stepSuggestion3 = new StepSuggestion();
        stepSuggestion3.setContent("content3");
        stepSuggestion3.setType(2);

        ArrayList<StepSuggestion> stepSuggestions = new ArrayList<>();
        stepSuggestions.add(stepSuggestion1);
        stepSuggestions.add(stepSuggestion2);
        stepSuggestions.add(stepSuggestion3);

        stepSuggestionDAO.save(stepSuggestions);

        stepSuggestionDAO.delete(stepSuggestions);

        Assert.assertTrue(stepSuggestionDAO.findAll().isEmpty());
    }
}
