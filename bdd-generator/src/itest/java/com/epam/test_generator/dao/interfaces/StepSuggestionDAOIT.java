package com.epam.test_generator.dao.interfaces;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class StepSuggestionDAOIT {

    @Autowired
    StepSuggestionDAO stepSuggestionDAO;

    @Test
    public void createAndRetrieve_StepSuggestion_Success() {
        StepSuggestion originalStepSuggestion = retrieveStepSuggestion();

        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        StepSuggestion newStepSuggestion = retrieveStepSuggestion();
        newStepSuggestion.setId(id);

        Assert.assertTrue(stepSuggestionDAO.findById(id).isPresent());

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findById(id).get());
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

        Assert.assertTrue(stepSuggestionDAO.findById(id).isPresent());

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findById(id).get());
    }

    @Test
    public void updateType_StepSuggestion_Success() {
        StepSuggestion originalStepSuggestion = retrieveStepSuggestion();

        stepSuggestionDAO.save(originalStepSuggestion);
        originalStepSuggestion.setType(StepType.ANY);
        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        StepSuggestion newStepSuggestion = retrieveStepSuggestion();
        newStepSuggestion.setId(id);
        newStepSuggestion.setType(StepType.ANY);

        Assert.assertTrue(stepSuggestionDAO.findById(id).isPresent());

        Assert.assertEquals(newStepSuggestion, stepSuggestionDAO.findById(id).get());
    }

    @Test
    public void removeById_StepSuggestion_Success() {
        StepSuggestion originalStepSuggestion = retrieveStepSuggestion();
        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        stepSuggestionDAO.deleteById(id);

        Assert.assertFalse(stepSuggestionDAO.existsById(id));
    }

    @Test
    public void remove_StepSuggestion_Success() {
        StepSuggestion originalStepSuggestion = retrieveStepSuggestion();

        long id = stepSuggestionDAO.save(originalStepSuggestion).getId();

        stepSuggestionDAO.delete(originalStepSuggestion);

        Assert.assertFalse(stepSuggestionDAO.existsById(id));
    }

    @Test
    public void addList_StepSuggestions_Success() {
        List<StepSuggestion> stepSuggestions = retrieveStepSuggestionList();

        int expectedSize = stepSuggestionDAO.findAll().size();

        List<Long> ids = stepSuggestionDAO.saveAll(stepSuggestions).stream().map(
            StepSuggestion::getId)
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

        int expectedSize = stepSuggestionDAO.findAll().size();

        stepSuggestions = stepSuggestionDAO.saveAll(stepSuggestions);

        stepSuggestionDAO.deleteAll(stepSuggestions);

        Assert.assertEquals(expectedSize, stepSuggestionDAO.findAll().size());
    }

    private StepSuggestion retrieveStepSuggestion() {
        StepSuggestion stepSuggestion = new StepSuggestion();
        stepSuggestion.setId(1L);
        stepSuggestion.setContent("content");
        stepSuggestion.setType(StepType.THEN);

        return stepSuggestion;
    }

    private List<StepSuggestion> retrieveStepSuggestionList() {
        StepSuggestion stepSuggestion1 = new StepSuggestion();
        stepSuggestion1.setId(0L);
        stepSuggestion1.setContent("content1");
        stepSuggestion1.setType(StepType.THEN);

        StepSuggestion stepSuggestion2 = new StepSuggestion();
        stepSuggestion2.setId(0L);
        stepSuggestion2.setContent("content2");
        stepSuggestion2.setType(StepType.WHEN);

        StepSuggestion stepSuggestion3 = new StepSuggestion();
        stepSuggestion3.setId(0L);
        stepSuggestion3.setContent("content3");
        stepSuggestion3.setType(StepType.THEN);

        ArrayList<StepSuggestion> stepSuggestions = new ArrayList<>();
        stepSuggestions.add(stepSuggestion1);
        stepSuggestions.add(stepSuggestion2);
        stepSuggestions.add(stepSuggestion3);

        return stepSuggestions;
    }
}
