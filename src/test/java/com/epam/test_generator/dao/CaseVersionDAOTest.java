package com.epam.test_generator.dao;

import static org.junit.Assert.*;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.config.JaversConfig;
import com.epam.test_generator.dao.impl.CaseVersionDAOImpl;
import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.pojo.CaseVersion;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    private Case caze;

    @Before
    public void setUp() {

        caze = new Case(CASE_ID, "description", Lists.newArrayList(), 1,
            Sets.newHashSet());
    }

    @Test
    public void testCommits() {
        caseVersionDAO.save(caze);

        caze.setDescription("descriptionUpdate");
        caseVersionDAO.save(caze);

        caze.setPriority(2);
        caseVersionDAO.save(caze);

        List<CaseVersion> caseVersionList = caseVersionDAO.find(CASE_ID);

        assertEquals(3, caseVersionList.size());
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
}