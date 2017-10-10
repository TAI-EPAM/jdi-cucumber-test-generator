package com.epam.test_generator.dao;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.entities.Case;
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

    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    @Test
    public void testCreateAndRetrieve() {
        Case originalCase = new Case();
        originalCase.setDescription("Case1 description");
        originalCase.setPriority(3);
        originalCase.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        originalCase.setSteps(new ArrayList<>());
        originalCase.setTags(new HashSet<>());
        originalCase.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));
        long id = caseDAO.save(originalCase).getId();

        Case newCase = new Case();
        newCase.setId(id);
        newCase.setDescription("Case1 description");
        newCase.setPriority(3);
        newCase.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        newCase.setSteps(new ArrayList<>());
        newCase.setTags(new HashSet<>());
        newCase.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        Assert.assertEquals(newCase, caseDAO.findOne(id));
    }

    @Test
    public void testUpdateDescription() {
        Case originalCase = new Case();
        originalCase.setDescription("Case1 description");
        originalCase.setPriority(3);
        originalCase.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        originalCase.setSteps(new ArrayList<>());
        originalCase.setTags(new HashSet<>());
        originalCase.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));
        caseDAO.save(originalCase);
        originalCase.setDescription("modified description");
        long id = caseDAO.save(originalCase).getId();

        Case newCase = new Case();
        newCase.setId(id);
        newCase.setDescription("modified description");
        newCase.setPriority(3);
        newCase.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        newCase.setSteps(new ArrayList<>());
        newCase.setTags(new HashSet<>());
        newCase.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        Assert.assertEquals(newCase, caseDAO.findOne(id));
    }

    @Test
    public void testUpdatePriority() {
        Case originalCase = new Case();
        originalCase.setDescription("Case1 description");
        originalCase.setPriority(3);
        originalCase.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        originalCase.setSteps(new ArrayList<>());
        originalCase.setTags(new HashSet<>());
        originalCase.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));
        caseDAO.save(originalCase);
        originalCase.setPriority(5);
        long id = caseDAO.save(originalCase).getId();

        Case newCase = new Case();
        newCase.setId(id);
        newCase.setDescription("Case1 description");
        newCase.setPriority(5);
        newCase.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        newCase.setSteps(new ArrayList<>());
        newCase.setTags(new HashSet<>());
        newCase.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));
        caseDAO.save(newCase);
        newCase.setPriority(5);

        Assert.assertEquals(newCase, caseDAO.findOne(id));
    }

    @Test
    public void testChangeUpdateDate() {
        Case originalCase = new Case();
        originalCase.setDescription("Case1 description");
        originalCase.setPriority(3);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.SEPTEMBER, 9);
        originalCase.setCreationDate(formatter.format(calendar.getTime()));
        originalCase.setSteps(new ArrayList<>());
        originalCase.setTags(new HashSet<>());
        originalCase.setUpdateDate(formatter.format(calendar.getTime()));
        caseDAO.save(originalCase);
        originalCase.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));
        long id = caseDAO.save(originalCase).getId();

        Case newCase = new Case();
        newCase.setId(id);
        newCase.setDescription("Case1 description");
        newCase.setPriority(3);
        newCase.setCreationDate(formatter.format(calendar.getTime()));
        newCase.setSteps(new ArrayList<>());
        newCase.setTags(new HashSet<>());
        newCase.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        Assert.assertEquals(newCase, caseDAO.findOne(id));
    }

    @Test
    public void testRemoveById() {
        Case originalCase = new Case();
        originalCase.setDescription("Case1 description");
        originalCase.setPriority(3);
        originalCase.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        originalCase.setSteps(new ArrayList<>());
        originalCase.setTags(new HashSet<>());
        originalCase.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));
        long id = caseDAO.save(originalCase).getId();
        caseDAO.delete(id);

        Assert.assertTrue(!caseDAO.exists(id));
    }

    @Test
    public void testRemove() {
        Case originalCase = new Case();
        originalCase.setDescription("Case1 description");
        originalCase.setPriority(3);
        originalCase.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        originalCase.setSteps(new ArrayList<>());
        originalCase.setTags(new HashSet<>());
        originalCase.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));
        long id = caseDAO.save(originalCase).getId();
        caseDAO.delete(originalCase);

        Assert.assertTrue(!caseDAO.exists(id));
    }

    @Test
    public void testAddList() {
        Case case1 = new Case();
        case1.setDescription("Case1 description");
        case1.setPriority(3);
        case1.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        case1.setSteps(new ArrayList<>());
        case1.setTags(new HashSet<>());
        case1.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        Case case2 = new Case();
        case2.setDescription("Case2 description");
        case2.setPriority(1);
        case2.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        case2.setSteps(new ArrayList<>());
        case2.setTags(new HashSet<>());
        case2.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        Case case3 = new Case();
        case3.setDescription("Case1 description");
        case3.setPriority(3);
        case3.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        case3.setSteps(new ArrayList<>());
        case3.setTags(new HashSet<>());
        case3.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        ArrayList<Case> cases = new ArrayList<>();
        cases.add(case1);
        cases.add(case2);
        cases.add(case3);

        List<Long> ids = caseDAO.save(cases).stream().map(Case::getId).collect(Collectors.toList());

        Case case4 = new Case();
        case4.setDescription("Case1 description");
        case4.setId(ids.get(0));
        case4.setPriority(3);
        case4.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        case4.setSteps(new ArrayList<>());
        case4.setTags(new HashSet<>());
        case4.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        Case case5 = new Case();
        case5.setDescription("Case2 description");
        case5.setId(ids.get(1));
        case5.setPriority(1);
        case5.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        case5.setSteps(new ArrayList<>());
        case5.setTags(new HashSet<>());
        case5.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        Case case6 = new Case();
        case6.setDescription("Case1 description");
        case6.setId(ids.get(2));
        case6.setPriority(3);
        case6.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        case6.setSteps(new ArrayList<>());
        case6.setTags(new HashSet<>());
        case6.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        ArrayList<Case> newCases = new ArrayList<>();

        newCases.add(case4);
        newCases.add(case5);
        newCases.add(case6);

        Assert.assertTrue(newCases.equals(caseDAO.findAll()));
    }

    @Test
    public void testRemoveList() {
        Case case1 = new Case();
        case1.setDescription("Case1 description");
        case1.setPriority(3);
        case1.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        case1.setSteps(new ArrayList<>());
        case1.setTags(new HashSet<>());
        case1.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        Case case2 = new Case();
        case2.setDescription("Case2 description");
        case2.setPriority(1);
        case2.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        case2.setSteps(new ArrayList<>());
        case2.setTags(new HashSet<>());
        case2.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        Case case3 = new Case();
        case3.setDescription("Case1 description");
        case3.setPriority(3);
        case3.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        case3.setSteps(new ArrayList<>());
        case3.setTags(new HashSet<>());
        case3.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        Case[] cases = {case1, case2, case3};

        caseDAO.save(Arrays.asList(cases));

        caseDAO.delete(Arrays.asList(cases));

        Assert.assertTrue(caseDAO.findAll().isEmpty());
    }
}