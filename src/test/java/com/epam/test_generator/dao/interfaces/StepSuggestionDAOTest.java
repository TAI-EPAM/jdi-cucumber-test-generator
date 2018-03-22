package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.DatabaseConfigForTests;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class StepSuggestionDAOTest {

    @Autowired
    StepSuggestionDAO stepSuggestionDAO;

    @Test
    public void createAndRetrieve_StepSuggestion_Success() {
        StepSuggestion originalStepSuggestion = retrieveStepSuggestion();

        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        StepSuggestion newStepSuggestion = retrieveStepSuggestion();
        newStepSuggestion.setId(id);

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findOne(id));
    }

    @Test
    public void updateContent_StepSuggestion_Success() {
        StepSuggestion originalStepSuggestion = retrieveStepSuggestion();

        stepSuggestionDAO.save(originalStepSuggestion);
        originalStepSuggestion.setContent("new content");
        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        StepSuggestion newStepSuggestion = retrieveStepSuggestion();
        newStepSuggestion.setId(id);
        newStepSuggestion.setContent("new content");

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findOne(id));
    }

    @Test
    public void updateType_StepSuggestion_Success() {
        StepSuggestion originalStepSuggestion = retrieveStepSuggestion();

        stepSuggestionDAO.save(originalStepSuggestion);
        originalStepSuggestion.setType(StepType.AND);
        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        StepSuggestion newStepSuggestion = retrieveStepSuggestion();
        newStepSuggestion.setId(id);
        newStepSuggestion.setType(StepType.AND);

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findOne(id));
    }

    @Test
    public void removeById_StepSuggestion_Success() {
        StepSuggestion originalStepSuggestion = retrieveStepSuggestion();
        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        stepSuggestionDAO.delete(id);

        Assert.assertTrue(!stepSuggestionDAO.exists(id));
    }

    @Test
    public void remove_StepSuggestion_Success() {
        StepSuggestion originalStepSuggestion = retrieveStepSuggestion();

        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        stepSuggestionDAO.delete(originalStepSuggestion);

        Assert.assertTrue(!stepSuggestionDAO.exists(id));
    }

    @Test
    public void addList_StepSuggestions_Success() {
        List<StepSuggestion> stepSuggestions = retrieveStepSuggestionList();

        int expectedSize = stepSuggestionDAO.findAll().size();

        List<Long> ids = stepSuggestionDAO.save(stepSuggestions).stream().map(StepSuggestion::getId)
            .collect(Collectors.toList());

        List<StepSuggestion> newStepSuggestions = retrieveStepSuggestionList();

        newStepSuggestions.get(0).setId(ids.get(0));
        newStepSuggestions.get(1).setId(ids.get(1));
        newStepSuggestions.get(2).setId(ids.get(2));

        expectedSize += newStepSuggestions.size();

        Assert.assertEquals(expectedSize, stepSuggestionDAO.findAll().size());
    }

    @Test
    public void removeList_StepSuggestions_Success() {
        List<StepSuggestion> stepSuggestions = retrieveStepSuggestionList();

        final int expectedSize = stepSuggestionDAO.findAll().size();

        stepSuggestions =  stepSuggestionDAO.save(stepSuggestions);

        stepSuggestionDAO.delete(stepSuggestions);

        Assert.assertEquals(expectedSize, stepSuggestionDAO.findAll().size());
    }

    private StepSuggestion retrieveStepSuggestion() {
        StepSuggestion stepSuggestion = new StepSuggestion();
        stepSuggestion.setId(1L);
        stepSuggestion.setContent("content");
        stepSuggestion.setType(StepType.THEN);
        stepSuggestion.setVersion(0L);

        return stepSuggestion;
    }

    private List<StepSuggestion> retrieveStepSuggestionList() {
        StepSuggestion stepSuggestion1 = new StepSuggestion();
        stepSuggestion1.setId(0L);
        stepSuggestion1.setContent("content1");
        stepSuggestion1.setType(StepType.THEN);
        stepSuggestion1.setVersion(0L);

        StepSuggestion stepSuggestion2 = new StepSuggestion();
        stepSuggestion2.setId(0L);
        stepSuggestion2.setContent("content2");
        stepSuggestion2.setType(StepType.WHEN);
        stepSuggestion2.setVersion(0L);

        StepSuggestion stepSuggestion3 = new StepSuggestion();
        stepSuggestion3.setId(0L);
        stepSuggestion3.setContent("content3");
        stepSuggestion3.setType(StepType.THEN);
        stepSuggestion3.setVersion(0L);

        ArrayList<StepSuggestion> stepSuggestions = new ArrayList<>();
        stepSuggestions.add(stepSuggestion1);
        stepSuggestions.add(stepSuggestion2);
        stepSuggestions.add(stepSuggestion3);

        return stepSuggestions;
    }
}
