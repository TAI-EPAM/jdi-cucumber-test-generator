package com.epam.test_generator.dao.interfaces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.config.JaversConfig;
import com.epam.test_generator.dao.JaversChangedDataExtractor;
import com.epam.test_generator.dao.impl.SuitVersionDAOImpl;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.pojo.PropertyDifference;
import com.epam.test_generator.pojo.SuitVersion;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
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
    SuitVersionDAOImpl.class, JaversChangedDataExtractor.class})
@Transactional
public class SuitVersionDAOTest {

    private static final Long SUIT_ID = 1L;
    private static final Long SUIT_ID2 = 2L;

    @Autowired
    private SuitVersionDAO suitVersionDAO;

    private Suit suit;
    private List<Suit> suitList;

    @Before
    public void setUp() {

        suit = new Suit(SUIT_ID, "name", "description", Lists.newArrayList(), 1,
            Sets.newHashSet(), 1);
        Suit suit2 = new Suit(SUIT_ID2, "name", "description2", Lists.newArrayList(), 1,
            Sets.newHashSet(), 1);
        suitList = Arrays.asList(suit, suit2);

    }

    @Test
    public void save_CreateAndEditSimpleSuit_SavedAllVersions() {
        suitVersionDAO.save(suit);

        suit.setName("nameUpdate");
        suitVersionDAO.save(suit);

        suit.setDescription("descriptionUpdate");
        suitVersionDAO.save(suit);

        suit.setPriority(2);
        suitVersionDAO.save(suit);

        List<SuitVersion> suitVersionList = suitVersionDAO.findAll(SUIT_ID);

        assertEquals(4, suitVersionList.size());
    }

    @Test
    public void find_SaveEditedSuit_GetHistoryWithEditedSuit() {
        suitVersionDAO.save(suit);

        String newDescription = "newDescription";
        suit.setDescription(newDescription);

        suitVersionDAO.save(suit);

        SuitVersion suitVersion = suitVersionDAO.findAll(SUIT_ID).get(1);

        assertEquals(1, suitVersion.getPropertyDifferences().size());
        assertEquals(new PropertyDifference(
            "description",
            "description",
            newDescription
        ), suitVersion.getPropertyDifferences().get(0));
    }

    @Test
    public void find_SaveAddedAndEditedCaseInSuit_GetHistoryWithAddedAndEditedCase() {
        suitVersionDAO.save(suit);

        Case caze = new Case(1L, "name", "description", Collections.emptyList(), 1,
            Collections.emptySet(), "comment");

        suit.addCase(caze);
        suitVersionDAO.save(suit);

        caze.setDescription("123");
        suitVersionDAO.save(suit);

        SuitVersion suitVersionAddedCase = suitVersionDAO.findAll(SUIT_ID).get(1);
        SuitVersion suitVersionEditedCase = suitVersionDAO.findAll(SUIT_ID).get(2);

        Case originalCase = new Case(1L, "name", "description", Collections.emptyList(), 1,
            Collections.emptySet(), "comment");
        originalCase.setRowNumber(1);
        assertEquals(2, suitVersionAddedCase.getPropertyDifferences().size());
        assertEquals(1, suitVersionEditedCase.getPropertyDifferences().size());

        assertEquals(new PropertyDifference(
            "status",
            Status.NOT_RUN,
            Status.NOT_DONE
        ), suitVersionAddedCase.getPropertyDifferences().get(0));

        assertEquals(new PropertyDifference(
            "cases",
            null,
            originalCase
        ), suitVersionAddedCase.getPropertyDifferences().get(1));

        assertEquals(new PropertyDifference(
            "cases",
            originalCase,
            caze
        ), suitVersionEditedCase.getPropertyDifferences().get(0));
    }

    @Test
    public void find_SaveAddedAndEditedTagInSuit_GetHistoryWithAddedAndEditedTag() {
        suitVersionDAO.save(suit);

        Tag tag = new Tag("name");

        suit.addTag(tag);
        suitVersionDAO.save(suit);

        tag.setName("editedName");
        suitVersionDAO.save(suit);

        SuitVersion suitVersionAddedTag = suitVersionDAO.findAll(SUIT_ID).get(1);
        SuitVersion suitVersionEditedTag = suitVersionDAO.findAll(SUIT_ID).get(2);

        Tag originalTag = new Tag("name");

        assertEquals(1, suitVersionAddedTag.getPropertyDifferences().size());
        assertEquals(2, suitVersionEditedTag.getPropertyDifferences().size());

        assertEquals(new PropertyDifference(
            "tags",
            null,
            originalTag
        ), suitVersionAddedTag.getPropertyDifferences().get(0));

        assertEquals(new PropertyDifference(
            "tags",
            originalTag,
            null
        ), suitVersionEditedTag.getPropertyDifferences().get(0));

        assertEquals(new PropertyDifference(
            "tags",
            null,
            tag
        ), suitVersionEditedTag.getPropertyDifferences().get(1));

    }

    @Test
    public void find_SaveEditedStatus_GetHistoryWithEditedStatus() {
        suit.setStatus(Status.NOT_RUN);
        suitVersionDAO.save(suit);

        Status status = Status.PASSED;
        suit.setStatus(status);

        suitVersionDAO.save(suit);

        SuitVersion suitVersion = suitVersionDAO.findAll(SUIT_ID).get(1);

        assertEquals(1, suitVersion.getPropertyDifferences().size());
        assertEquals(new PropertyDifference(
            "status",
            Status.NOT_RUN,
            Status.PASSED
        ), suitVersion.getPropertyDifferences().get(0));
    }

    @Test
    public void findById_ExistingId_Found() {
        suitVersionDAO.save(suit);

        List<SuitVersion> suitVersions = suitVersionDAO.findAll(SUIT_ID);

        Suit actualSuit = suitVersionDAO.findByCommitId(SUIT_ID, suitVersions.get(0).getCommitId());

        assertEquals(suit, actualSuit);
    }

    @Test
    public void findById_WrongId_ReturnNull() {
        Suit actualSuit = suitVersionDAO.findByCommitId(SUIT_ID, "5.3");

        assertNull(actualSuit);
    }

    @Test
    public void delete_SimpleSuit_Deleted() {
        suitVersionDAO.save(suit);
        suitVersionDAO.delete(suit);

        List<SuitVersion> suitVersionList = suitVersionDAO.findAll(SUIT_ID);

        assertEquals(2, suitVersionList.size());
    }

    @Test
    public void delete_SimpleSuitList_Deleted() {
        suitVersionDAO.save(suitList);
        suitVersionDAO.delete(suitList);

        List<SuitVersion> suitVersions = suitVersionDAO.findAll(SUIT_ID);
        List<SuitVersion> suitVersions2 = suitVersionDAO.findAll(SUIT_ID2);

        assertEquals(2, suitVersions.size());
        assertEquals(2, suitVersions2.size());

    }

    @Test
    public void save_SimpleSuitList_Saved() {
        suitVersionDAO.save(suitList);

        List<SuitVersion> suitVersions = suitVersionDAO.findAll(SUIT_ID);
        List<SuitVersion> suitVersions2 = suitVersionDAO.findAll(SUIT_ID2);

        assertEquals(1, suitVersions.size());
        assertEquals(1, suitVersions2.size());
    }

    @Test
    public void delete_NullSimpleSuitList_NothingDeleted() {
        suitVersionDAO.save(suitList);
        suitVersionDAO.delete((Iterable<Suit>) null);

        List<SuitVersion> suitVersions = suitVersionDAO.findAll(SUIT_ID);
        List<SuitVersion> suitVersions2 = suitVersionDAO.findAll(SUIT_ID2);

        assertEquals(1, suitVersions.size());
        assertEquals(1, suitVersions2.size());
    }

    @Test
    public void delete_NullSimpleSuitList_NothingSaved() {
        suitVersionDAO.save((Iterable<Suit>) null);

        List<SuitVersion> suitVersions = suitVersionDAO.findAll(SUIT_ID);
        List<SuitVersion> suitVersions2 = suitVersionDAO.findAll(SUIT_ID2);

        assertEquals(0, suitVersions.size());
        assertEquals(0, suitVersions2.size());
    }
}
