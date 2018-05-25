package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.entities.DefaultStepSuggestion;
import com.epam.test_generator.entities.StepType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class DefaultDefaultStepSuggestionDAOTest {

    @Autowired
    DefaultStepSuggestionDAO defaultStepSuggestionDAO;

    @Test
    public void createAndRetrieve_StepSuggestion_Success() {
        DefaultStepSuggestion originalDefaultStepSuggestion = retrieveStepSuggestion();

        long id = defaultStepSuggestionDAO.save(originalDefaultStepSuggestion).getId();

        DefaultStepSuggestion newDefaultStepSuggestion = retrieveStepSuggestion();
        newDefaultStepSuggestion.setId(id);

        Assert.assertTrue(defaultStepSuggestionDAO.findById(id).isPresent());

        Assert.assertEquals(newDefaultStepSuggestion, defaultStepSuggestionDAO.findById(id).get());
    }

    @Test
    public void updateContent_StepSuggestion_Success() {
        DefaultStepSuggestion originalDefaultStepSuggestion = retrieveStepSuggestion();

        defaultStepSuggestionDAO.save(originalDefaultStepSuggestion);
        originalDefaultStepSuggestion.setContent("new content");
        long id = defaultStepSuggestionDAO.save(originalDefaultStepSuggestion).getId();

        DefaultStepSuggestion newDefaultStepSuggestion = retrieveStepSuggestion();
        newDefaultStepSuggestion.setId(id);
        newDefaultStepSuggestion.setContent("new content");

        Assert.assertTrue(defaultStepSuggestionDAO.findById(id).isPresent());

        Assert.assertEquals(newDefaultStepSuggestion, defaultStepSuggestionDAO.findById(id).get());
    }

    @Test
    public void updateType_StepSuggestion_Success() {
        DefaultStepSuggestion originalDefaultStepSuggestion = retrieveStepSuggestion();

        defaultStepSuggestionDAO.save(originalDefaultStepSuggestion);
        originalDefaultStepSuggestion.setType(StepType.AND);
        long id = defaultStepSuggestionDAO.save(originalDefaultStepSuggestion).getId();

        DefaultStepSuggestion newDefaultStepSuggestion = retrieveStepSuggestion();
        newDefaultStepSuggestion.setId(id);
        newDefaultStepSuggestion.setType(StepType.AND);

        Assert.assertTrue(defaultStepSuggestionDAO.findById(id).isPresent());

        Assert.assertEquals(newDefaultStepSuggestion, defaultStepSuggestionDAO.findById(id).get());
    }

    @Test
    public void removeById_StepSuggestion_Success() {
        DefaultStepSuggestion originalDefaultStepSuggestion = retrieveStepSuggestion();
        long id = defaultStepSuggestionDAO.save(originalDefaultStepSuggestion).getId();

        defaultStepSuggestionDAO.deleteById(id);

        Assert.assertFalse(defaultStepSuggestionDAO.existsById(id));
    }

    @Test
    public void remove_StepSuggestion_Success() {
        DefaultStepSuggestion originalDefaultStepSuggestion = retrieveStepSuggestion();

        long id = defaultStepSuggestionDAO.save(originalDefaultStepSuggestion).getId();

        defaultStepSuggestionDAO.delete(originalDefaultStepSuggestion);

        Assert.assertFalse(defaultStepSuggestionDAO.existsById(id));
    }

    @Test
    public void addList_StepSuggestions_Success() {
        List<DefaultStepSuggestion> defaultStepSuggestions = retrieveStepSuggestionList();

        int expectedSize = defaultStepSuggestionDAO.findAll().size();

        List<Long> ids = defaultStepSuggestionDAO.saveAll(defaultStepSuggestions).stream().map(
            DefaultStepSuggestion::getId)
            .collect(Collectors.toList());

        List<DefaultStepSuggestion> newDefaultStepSuggestions = retrieveStepSuggestionList();

        newDefaultStepSuggestions.get(0).setId(ids.get(0));
        newDefaultStepSuggestions.get(1).setId(ids.get(1));
        newDefaultStepSuggestions.get(2).setId(ids.get(2));

        expectedSize += newDefaultStepSuggestions.size();

        Assert.assertEquals(expectedSize, defaultStepSuggestionDAO.findAll().size());
    }

    @Test
    public void removeList_StepSuggestions_Success() {
        List<DefaultStepSuggestion> defaultStepSuggestions = retrieveStepSuggestionList();

        int expectedSize = defaultStepSuggestionDAO.findAll().size();

        defaultStepSuggestions =  defaultStepSuggestionDAO.saveAll(defaultStepSuggestions);

        defaultStepSuggestionDAO.deleteAll(defaultStepSuggestions);

        Assert.assertEquals(expectedSize, defaultStepSuggestionDAO.findAll().size());
    }

    @Test
    public void findByContentIgnoreCaseContaining_SearchString_Success() {
        List<DefaultStepSuggestion> defaultStepSuggestions = retrieveStepSuggestionList();
        defaultStepSuggestions =  defaultStepSuggestionDAO.saveAll(defaultStepSuggestions);

        PageRequest numberOfReturnedResults = new PageRequest(0, 10);
        List<DefaultStepSuggestion> content = defaultStepSuggestionDAO
            .findByContentIgnoreCaseContaining("content", numberOfReturnedResults);

        Assert.assertEquals(defaultStepSuggestions.size(), content.size());
        Assert.assertEquals(defaultStepSuggestions, content);
    }

    private DefaultStepSuggestion retrieveStepSuggestion() {
        DefaultStepSuggestion defaultStepSuggestion = new DefaultStepSuggestion();
        defaultStepSuggestion.setId(1L);
        defaultStepSuggestion.setContent("content");
        defaultStepSuggestion.setType(StepType.THEN);

        return defaultStepSuggestion;
    }

    private List<DefaultStepSuggestion> retrieveStepSuggestionList() {
        DefaultStepSuggestion defaultStepSuggestion1 = new DefaultStepSuggestion();
        defaultStepSuggestion1.setId(0L);
        defaultStepSuggestion1.setContent("content1");
        defaultStepSuggestion1.setType(StepType.THEN);

        DefaultStepSuggestion defaultStepSuggestion2 = new DefaultStepSuggestion();
        defaultStepSuggestion2.setId(0L);
        defaultStepSuggestion2.setContent("content2");
        defaultStepSuggestion2.setType(StepType.WHEN);

        DefaultStepSuggestion defaultStepSuggestion3 = new DefaultStepSuggestion();
        defaultStepSuggestion3.setId(0L);
        defaultStepSuggestion3.setContent("content3");
        defaultStepSuggestion3.setType(StepType.THEN);

        ArrayList<DefaultStepSuggestion> defaultStepSuggestions = new ArrayList<>();
        defaultStepSuggestions.add(defaultStepSuggestion1);
        defaultStepSuggestions.add(defaultStepSuggestion2);
        defaultStepSuggestions.add(defaultStepSuggestion3);

        return defaultStepSuggestions;
    }
}
