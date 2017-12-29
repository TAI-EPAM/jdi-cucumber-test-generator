package com.epam.test_generator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.config.JaversConfig;
import com.epam.test_generator.dao.impl.CaseVersionDAOImpl;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.pojo.CaseVersion;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class, JaversConfig.class,
    CaseVersionDAOImpl.class})
@Transactional
public class CaseVersionDAOTest {

    private static final Long CASE_ID = 1L;
    private static final Long CASE_ID2 = 2L;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    private Case caze;
    private List<Case> caseList;

    @Before
    public void setUp() {

        caze = new Case(CASE_ID, "name", "description", Lists.newArrayList(), 1,
            Sets.newHashSet());
        Case caze2 = new Case(CASE_ID2, "name", "description2", Lists.newArrayList(), 1,
            Sets.newHashSet());
        caseList = Arrays.asList(caze, caze2);

    }

    @Test
    public void testCommits() {
        caseVersionDAO.save(caze);

        caze.setName("nameUpdate");
        caseVersionDAO.save(caze);

        caze.setDescription("descriptionUpdate");
        caseVersionDAO.save(caze);

        caze.setPriority(2);
        caseVersionDAO.save(caze);

        final List<CaseVersion> caseVersionList = caseVersionDAO.find(CASE_ID);

        assertEquals(4, caseVersionList.size());
    }

    @Test
    public void testProperty() {
        caseVersionDAO.save(caze);

        CaseVersion caseVersion = caseVersionDAO.find(CASE_ID).get(0);

        assertEquals(caze, caseVersion.getCaze());
    }

    @Test
    public void testStepSave() {
        Step step = new Step(1L, 1, "description", StepType.GIVEN);
        caze.addStep(step);

        caseVersionDAO.save(caze);

        CaseVersion caseVersion = caseVersionDAO.find(CASE_ID).get(0);

        assertEquals(step, caseVersion.getCaze().getSteps().get(0));
    }

    @Test
    public void testTagSave() {
        Tag tag = new Tag("name");
        tag.setId(1L);

        caze.addTag(tag);

        caseVersionDAO.save(caze);

        CaseVersion caseVersion = caseVersionDAO.find(CASE_ID).get(0);

        assertTrue(caseVersion.getCaze().getTags().contains(tag));
    }

    @Test
    public void testStatusSave() {
        Status status = Status.PASSED;
        caze.setStatus(status);

        caseVersionDAO.save(caze);

        CaseVersion caseVersion = caseVersionDAO.find(CASE_ID).get(0);

        assertEquals(status, caseVersion.getCaze().getStatus());
    }

    @Test
    public void testDelete() {
        caseVersionDAO.save(caze);
        caseVersionDAO.delete(caze);

        List<CaseVersion> caseVersionList = caseVersionDAO.find(CASE_ID);

        assertEquals(2, caseVersionList.size());
    }

    @Test
    public void delete_SimpleCaseList_Deleted() {
        caseVersionDAO.save(caseList);
        caseVersionDAO.delete(caseList);

        List<CaseVersion> caseVersions = caseVersionDAO.find(CASE_ID);
        List<CaseVersion> caseVersions2 = caseVersionDAO.find(CASE_ID2);

        assertEquals(2, caseVersions.size());
        assertEquals(2, caseVersions2.size());

    }

    @Test
    public void save_SimpleCaseList_Saved() {
        caseVersionDAO.save(caseList);

        List<CaseVersion> caseVersions = caseVersionDAO.find(CASE_ID);
        List<CaseVersion> caseVersions2 = caseVersionDAO.find(CASE_ID2);

        assertEquals(1, caseVersions.size());
        assertEquals(1, caseVersions2.size());
    }

    @Test
    public void delete_NullSimpleCaseList_NothingDeleted() {
        caseVersionDAO.save(caseList);
        caseVersionDAO.delete((Iterable<Case>) null);

        List<CaseVersion> caseVersions = caseVersionDAO.find(CASE_ID);
        List<CaseVersion> caseVersions2 = caseVersionDAO.find(CASE_ID2);

        assertEquals(1, caseVersions.size());
        assertEquals(1, caseVersions2.size());
    }

    @Test
    public void delete_NullSimpleCaseList_NothingSaved() {
        caseVersionDAO.save((Iterable<Case>) null);

        List<CaseVersion> caseVersions = caseVersionDAO.find(CASE_ID);
        List<CaseVersion> caseVersions2 = caseVersionDAO.find(CASE_ID2);

        assertEquals(0, caseVersions.size());
        assertEquals(0, caseVersions2.size());
    }
}