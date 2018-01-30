package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.StepSuggestionDAO;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.StepType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class StepSuggestionDAOTest {

    @Autowired
    StepSuggestionDAO stepSuggestionDAO;

    @Test
    public void testCreateAndRetrieve() {
        StepSuggestion originalStepSuggestion = retrieveStepUggestion();

        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        StepSuggestion newStepSuggestion = retrieveStepUggestion();
        newStepSuggestion.setId(id);

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findOne(id));
    }

    @Test
    public void testUpdateContent() {
        StepSuggestion originalStepSuggestion = retrieveStepUggestion();

        stepSuggestionDAO.save(originalStepSuggestion);
        originalStepSuggestion.setContent("new content");
        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        StepSuggestion newStepSuggestion = retrieveStepUggestion();
        newStepSuggestion.setId(id);
        newStepSuggestion.setContent("new content");

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findOne(id));
    }

    @Test
    public void testUpdateType() {
        StepSuggestion originalStepSuggestion = retrieveStepUggestion();

        stepSuggestionDAO.save(originalStepSuggestion);
        originalStepSuggestion.setType(StepType.AND);
        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        StepSuggestion newStepSuggestion = retrieveStepUggestion();
        newStepSuggestion.setId(id);
        newStepSuggestion.setType(StepType.AND);

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findOne(id));
    }

    @Test
    public void testRemoveById() {
        StepSuggestion originalStepSuggestion = retrieveStepUggestion();
        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        stepSuggestionDAO.delete(id);

        Assert.assertTrue(!stepSuggestionDAO.exists(id));
    }

    @Test
    public void testRemove() {
        StepSuggestion originalStepSuggestion = retrieveStepUggestion();

        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        stepSuggestionDAO.delete(originalStepSuggestion);

        Assert.assertTrue(!stepSuggestionDAO.exists(id));
    }

    @Test
    public void testAddList() {
        List<StepSuggestion> stepSuggestions = retrieveStepSuggestionList();

        List<Long> ids = stepSuggestionDAO.save(stepSuggestions).stream().map(StepSuggestion::getId)
            .collect(Collectors.toList());

        List<StepSuggestion> newStepSuggestions = retrieveStepSuggestionList();

        newStepSuggestions.get(0).setId(ids.get(0));
        newStepSuggestions.get(1).setId(ids.get(1));
        newStepSuggestions.get(2).setId(ids.get(2));

        Assert.assertTrue(newStepSuggestions.equals(stepSuggestionDAO.findAll()));
    }

    @Test
    public void testRemoveList() {
        List<StepSuggestion> stepSuggestions = retrieveStepSuggestionList();

        stepSuggestionDAO.save(stepSuggestions);

        stepSuggestionDAO.delete(stepSuggestions);

        Assert.assertTrue(stepSuggestionDAO.findAll().isEmpty());
    }

    private StepSuggestion retrieveStepUggestion() {
        StepSuggestion stepSuggestion = new StepSuggestion();
        stepSuggestion.setContent("content");
        stepSuggestion.setType(StepType.THEN);

        return stepSuggestion;
    }

    private List<StepSuggestion> retrieveStepSuggestionList() {
        StepSuggestion stepSuggestion1 = new StepSuggestion();
        stepSuggestion1.setContent("content1");
        stepSuggestion1.setType(StepType.THEN);

        StepSuggestion stepSuggestion2 = new StepSuggestion();
        stepSuggestion2.setContent("content2");
        stepSuggestion2.setType(StepType.WHEN);
        ;

        StepSuggestion stepSuggestion3 = new StepSuggestion();
        stepSuggestion3.setContent("content3");
        stepSuggestion3.setType(StepType.THEN);

        ArrayList<StepSuggestion> stepSuggestions = new ArrayList<>();
        stepSuggestions.add(stepSuggestion1);
        stepSuggestions.add(stepSuggestion2);
        stepSuggestions.add(stepSuggestion3);

        return stepSuggestions;
    }
}
