package com.epam.test_generator.dao;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={DatabaseConfigForTests.class})
@Transactional
public class CaseDAOTest {

    @Autowired
    CaseDAO caseDAO;

    @Test
    public void testCreateAndRetrieve() {
        Case originalCase = retrieveCase();
        long id = caseDAO.save(originalCase).getId();

        Case newCase = retrieveCase();
        newCase.setId(id);

        Assert.assertEquals(newCase, caseDAO.findOne(id));
    }

    @Test
    public void testUpdateDescription() {
        Case originalCase = retrieveCase();
        caseDAO.save(originalCase);
        originalCase.setDescription("modified description");
        long id = caseDAO.save(originalCase).getId();

        Case newCase = retrieveCase();
        newCase.setId(id);
        newCase.setDescription("modified description");

        Assert.assertEquals(newCase, caseDAO.findOne(id));
    }

    @Test
    public void testUpdatePriority() {
        Case originalCase = retrieveCase();
        caseDAO.save(originalCase);
        originalCase.setPriority(5);
        long id = caseDAO.save(originalCase).getId();

        Case newCase = retrieveCase();
        newCase.setId(id);
        newCase.setPriority(5);
        caseDAO.save(newCase);

        Assert.assertEquals(newCase, caseDAO.findOne(id));
    }

    @Test
    public void testChangeUpdateDate() {
        Case originalCase = retrieveCase();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.SEPTEMBER, 9);

        originalCase.setUpdateDate(calendar.getTime());
        originalCase.setCreationDate(calendar.getTime());
        caseDAO.save(originalCase);
        originalCase.setUpdateDate(Calendar.getInstance().getTime());
        long id = caseDAO.save(originalCase).getId();

        Case newCase = retrieveCase();
        newCase.setCreationDate(calendar.getTime());
        newCase.setId(id);

        Assert.assertEquals(newCase, caseDAO.findOne(id));
    }

    @Test
    public void testRemoveById() {
        Case originalCase = retrieveCase();
        long id = caseDAO.save(originalCase).getId();
        caseDAO.delete(id);

        Assert.assertTrue(!caseDAO.exists(id));
    }

    @Test
    public void testRemove() {
        Case originalCase = retrieveCase();
        long id = caseDAO.save(originalCase).getId();
        caseDAO.delete(originalCase);

        Assert.assertTrue(!caseDAO.exists(id));
    }

    @Test
    public void testAddList() {
        List<Case> cases = retrieveCaseList();

        List<Long> ids = caseDAO.save(cases).stream().map(Case::getId).collect(Collectors.toList());

        List<Case> newCases = retrieveCaseList();
        newCases.get(0).setId(ids.get(0));
        newCases.get(1).setId(ids.get(1));
        newCases.get(2).setId(ids.get(2));

        Assert.assertTrue(newCases.equals(caseDAO.findAll()));
    }

    @Test
    public void testRemoveList() {
        List<Case> cases = retrieveCaseList();

        caseDAO.save(cases);

        caseDAO.delete(cases);

        Assert.assertTrue(caseDAO.findAll().isEmpty());
    }

    private Case retrieveCase() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        return new Case("Case description", new ArrayList<>(),
                Calendar.getInstance().getTime(), Calendar.getInstance().getTime(),
                3, new HashSet<>(), Status.NOT_RUN);
    }

    private List<Case> retrieveCaseList() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        Case case1 = new Case("Case1 description", new ArrayList<>(), Calendar.getInstance().getTime(),
                Calendar.getInstance().getTime(), 3, new HashSet<>(), Status.NOT_RUN);
        Case case2 = new Case("Case2 description", new ArrayList<>(), Calendar.getInstance().getTime(),
                Calendar.getInstance().getTime(), 1, new HashSet<>(), Status.NOT_RUN);
        Case case3 = new Case("Case3 description", new ArrayList<>(), Calendar.getInstance().getTime(),
                Calendar.getInstance().getTime(), 3, new HashSet<>(), Status.NOT_RUN);

        ArrayList<Case> cases = new ArrayList<>();
        cases.add(case1);
        cases.add(case2);
        cases.add(case3);

        return cases;
    }
}