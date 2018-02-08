package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Tag;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
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
public class CaseDAOTest {

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private TagDAO tagDAO;

    @Test
    public void createAndRetrieve_CaseById_Valid() {
        Case originalCase = retrieveCase();
        long id = caseDAO.save(originalCase).getId();

        Case newCase = retrieveCase();
        newCase.setId(id);

        Assert.assertEquals(newCase, caseDAO.findOne(id));
    }

    @Test
    public void save_CaseWithNullIdAndNotNullTagId_Success() {
        Tag tagWithNullId = new Tag("tag with null id");
        Tag tagWithNotNullId = tagDAO.save(new Tag("tag with not null id"));

        Case caze = new Case("name", "desc", null, null, null, 3,
            Sets.newHashSet(tagWithNotNullId, tagWithNullId), null);
        Case savedCase = caseDAO.save(caze);

        caze.setId(savedCase.getId());

        Assert.assertEquals(caze, savedCase);
    }

    @Test
    public void updateName_Case_Success() {
        final Case originalCase = retrieveCase();
        caseDAO.save(originalCase);

        originalCase.setName("modified name");
        final long id = caseDAO.save(originalCase).getId();

        final Case newCase = retrieveCase();
        newCase.setId(id);
        newCase.setName("modified name");

        Assert.assertEquals(newCase.getName(), caseDAO.findOne(id).getName());

    }

    @Test
    public void updateDescription_Case_Success() {
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
    public void updatePriority_Case_Success() {
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
    public void updateDate_Case_Success() {
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
    public void removeById_Case_Success() {
        Case originalCase = retrieveCase();
        long id = caseDAO.save(originalCase).getId();
        caseDAO.delete(id);

        Assert.assertTrue(!caseDAO.exists(id));
    }

    @Test
    public void remove_Case_Success() {
        Case savedCase = caseDAO.save(retrieveCase());
        caseDAO.delete(savedCase);

        Assert.assertTrue(!caseDAO.exists(savedCase.getId()));
    }

    @Test
    public void addList_Cases_Success() {
        List<Case> cases = retrieveCaseList();

        List<Long> ids = caseDAO.save(cases).stream().map(Case::getId).collect(Collectors.toList());

        List<Case> newCases = retrieveCaseList();
        newCases.get(0).setId(ids.get(0));
        newCases.get(1).setId(ids.get(1));
        newCases.get(2).setId(ids.get(2));

        Assert.assertTrue(newCases.equals(caseDAO.findAll()));
    }

    @Test
    public void removeList_Cases_Success() {
        List<Case> savedCases = caseDAO.save(retrieveCaseList());

        caseDAO.delete(savedCases);

        Assert.assertTrue(caseDAO.findAll().isEmpty());
    }

    private Case retrieveCase() {

        return new Case("Case name", "Case description", new ArrayList<>(),
            Calendar.getInstance().getTime(), Calendar.getInstance().getTime(),
            3, new HashSet<>(), Status.NOT_RUN);
    }

    private List<Case> retrieveCaseList() {

        final Case case1 = new Case("Case1 name", "Case1 description", new ArrayList<>(),
            Calendar.getInstance().getTime(),
            Calendar.getInstance().getTime(), 3, new HashSet<>(), Status.NOT_RUN);
        final Case case2 = new Case("Case2 name", "Case2 description", new ArrayList<>(),
            Calendar.getInstance().getTime(),
            Calendar.getInstance().getTime(), 1, new HashSet<>(), Status.NOT_RUN);
        final Case case3 = new Case("Case3 name", "Case3 description", new ArrayList<>(),
            Calendar.getInstance().getTime(),
            Calendar.getInstance().getTime(), 3, new HashSet<>(), Status.NOT_RUN);

        ArrayList<Case> cases = new ArrayList<>();
        cases.add(case1);
        cases.add(case2);
        cases.add(case3);

        return cases;
    }
}