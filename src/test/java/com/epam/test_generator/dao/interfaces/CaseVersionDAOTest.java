package com.epam.test_generator.dao.interfaces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.config.JaversConfig;
import com.epam.test_generator.dao.JaversChangedDataExtractor;
import com.epam.test_generator.dao.impl.CaseVersionDAOImpl;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.pojo.CaseVersion;
import com.epam.test_generator.pojo.PropertyDifference;
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
    CaseVersionDAOImpl.class, JaversChangedDataExtractor.class})
@Transactional
public class CaseVersionDAOTest {

    private static final Long CASE_ID = 1L;
    private static final Long CASE_ID2 = 2L;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    private Case caze;
    private List<Case> caseList;

   // private TreeMap<CommitMetadata, List<Change>> treeOfChanges;

    @Before
    public void setUp() {

        caze = new Case(CASE_ID, "name", "description", Lists.newArrayList(), 1,
            Sets.newHashSet(), "comment");
        Case caze2 = new Case(CASE_ID2, "name", "description2", Lists.newArrayList(), 1,
            Sets.newHashSet(), "comment");
        caseList = Arrays.asList(caze, caze2);

    }

    @Test
    public void save_CreateAndEditSimpleCase_SavedAllVersions() {
        caseVersionDAO.save(caze);

        caze.setName("nameUpdate");
        caseVersionDAO.save(caze);

        caze.setDescription("descriptionUpdate");
        caseVersionDAO.save(caze);

        caze.setPriority(2);
        caseVersionDAO.save(caze);

        List<CaseVersion> caseVersionList = caseVersionDAO.findAll(CASE_ID);

        assertEquals(4, caseVersionList.size());
    }

    @Test
    public void find_SaveEditedCase_GetHistoryWithEditedCase() {
        caseVersionDAO.save(caze);

        String newDescription = "newDescription";
        caze.setDescription(newDescription);

        caseVersionDAO.save(caze);

        CaseVersion caseVersion = caseVersionDAO.findAll(CASE_ID).get(1);

        assertEquals(1, caseVersion.getPropertyDifferences().size());
        assertEquals(new PropertyDifference(
            "description",
            "description",
            newDescription
        ), caseVersion.getPropertyDifferences().get(0));
    }

    @Test
    public void find_SaveAddedAndEditedStepInCase_GetHistoryWithAddedAndEditedStep() {
        caseVersionDAO.save(caze);

        Step step = new Step(1L, 1, "description", StepType.GIVEN, "Comment", Status.NOT_RUN);

        caze.addStep(step);
        caseVersionDAO.save(caze);

        step.setDescription("123");
        caseVersionDAO.save(caze);

        CaseVersion caseVersionAddedStep = caseVersionDAO.findAll(CASE_ID).get(1);
        CaseVersion caseVersionEditedStep = caseVersionDAO.findAll(CASE_ID).get(2);

        Step originalStep = new Step(1L, 1, "description", StepType.GIVEN, "Comment",
            Status.NOT_RUN);
        assertEquals(2, caseVersionAddedStep.getPropertyDifferences().size());
        assertEquals(1, caseVersionEditedStep.getPropertyDifferences().size());

        assertEquals(new PropertyDifference(
            "steps",
            null,
            originalStep
        ), caseVersionAddedStep.getPropertyDifferences().get(0));

        assertEquals(new PropertyDifference(
            "steps",
            originalStep,
            step
        ), caseVersionEditedStep.getPropertyDifferences().get(0));
    }

    @Test
    public void find_SaveAddedAndEditedTagInCase_GetHistoryWithAddedAndEditedTag() {
        caseVersionDAO.save(caze);

        Tag tag = new Tag("name");

        caze.addTag(tag);
        caseVersionDAO.save(caze);

        tag.setName("editedName");
        caseVersionDAO.save(caze);

        CaseVersion caseVersionAddedTag = caseVersionDAO.findAll(CASE_ID).get(1);
        CaseVersion caseVersionEditedTag = caseVersionDAO.findAll(CASE_ID).get(2);

        Tag originalTag = new Tag("name");

        assertEquals(1, caseVersionAddedTag.getPropertyDifferences().size());
        assertEquals(2, caseVersionEditedTag.getPropertyDifferences().size());

        assertEquals(new PropertyDifference(
            "tags",
            null,
            originalTag
        ), caseVersionAddedTag.getPropertyDifferences().get(0));

        assertEquals(new PropertyDifference(
            "tags",
            originalTag,
            null
        ), caseVersionEditedTag.getPropertyDifferences().get(0));

        assertEquals(new PropertyDifference(
            "tags",
            null,
            tag
        ), caseVersionEditedTag.getPropertyDifferences().get(1));

    }

    @Test
    public void find_SaveEditedStatus_GetHistoryWithEditedStatus() {
        caze.setStatus(Status.NOT_RUN);
        caseVersionDAO.save(caze);

        Status status = Status.PASSED;
        caze.setStatus(status);

        caseVersionDAO.save(caze);

        CaseVersion caseVersion = caseVersionDAO.findAll(CASE_ID).get(1);

        assertEquals(1, caseVersion.getPropertyDifferences().size());
        assertEquals(new PropertyDifference(
            "status",
            Status.NOT_RUN,
            Status.PASSED
        ), caseVersion.getPropertyDifferences().get(0));
    }

    @Test
    public void findById_ExistingId_Found() {
        caseVersionDAO.save(caze);

        List<CaseVersion> caseVersions = caseVersionDAO.findAll(CASE_ID);

        Case actualCase = caseVersionDAO.findByCommitId(CASE_ID, caseVersions.get(0).getCommitId());

        assertEquals(caze, actualCase);
    }

    @Test
    public void findById_WrongId_ReturnNull() {
        Case actualCase = caseVersionDAO.findByCommitId(CASE_ID, "5.3");

        assertNull(actualCase);
    }

    @Test
    public void delete_SimpleCase_Deleted() {
        caseVersionDAO.save(caze);
        caseVersionDAO.delete(caze);

        List<CaseVersion> caseVersionList = caseVersionDAO.findAll(CASE_ID);

        assertEquals(2, caseVersionList.size());
    }

    @Test
    public void delete_SimpleCaseList_Deleted() {
        caseVersionDAO.save(caseList);
        caseVersionDAO.delete(caseList);

        List<CaseVersion> caseVersions = caseVersionDAO.findAll(CASE_ID);
        List<CaseVersion> caseVersions2 = caseVersionDAO.findAll(CASE_ID2);

        assertEquals(2, caseVersions.size());
        assertEquals(2, caseVersions2.size());

    }

    @Test
    public void save_SimpleCaseList_Saved() {
        caseVersionDAO.save(caseList);

        List<CaseVersion> caseVersions = caseVersionDAO.findAll(CASE_ID);
        List<CaseVersion> caseVersions2 = caseVersionDAO.findAll(CASE_ID2);

        assertEquals(1, caseVersions.size());
        assertEquals(1, caseVersions2.size());
    }

    @Test
    public void delete_NullSimpleCaseList_NothingDeleted() {
        caseVersionDAO.save(caseList);
        caseVersionDAO.delete((Iterable<Case>) null);

        List<CaseVersion> caseVersions = caseVersionDAO.findAll(CASE_ID);
        List<CaseVersion> caseVersions2 = caseVersionDAO.findAll(CASE_ID2);

        assertEquals(1, caseVersions.size());
        assertEquals(1, caseVersions2.size());
    }

    @Test
    public void delete_NullSimpleCaseList_NothingSaved() {
        caseVersionDAO.save((Iterable<Case>) null);

        List<CaseVersion> caseVersions = caseVersionDAO.findAll(CASE_ID);
        List<CaseVersion> caseVersions2 = caseVersionDAO.findAll(CASE_ID2);

        assertEquals(0, caseVersions.size());
        assertEquals(0, caseVersions2.size());
    }
}