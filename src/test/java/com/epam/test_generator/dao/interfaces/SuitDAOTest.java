package com.epam.test_generator.dao.interfaces;

import static org.mockito.Mockito.when;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class SuitDAOTest {

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private TagDAO tagDAO;

    @Test
    public void testCreateAndRetrieve() {
        final Suit originalSuit = retriveSuit();
        final long id = suitDAO.save(originalSuit).getId();

        final Suit newSuit = retriveSuit();

        final Set<Tag> tagsWithIds = suitDAO.findOne(id).getTags();
        final Set<Tag> tagsWithoutIds = newSuit.getTags();

        Assert.assertEquals(tagsWithIds.size(), tagsWithoutIds.size());

        final Set<Tag> unsavedTagsWithIds = setIdsForTags(tagsWithIds, tagsWithoutIds);

        newSuit.setId(id);
        newSuit.setTags(unsavedTagsWithIds);

        Assert.assertEquals(newSuit, suitDAO.findOne(id));
    }

    @Test
    public void save_SuitWithNullIdAndNotNullTagId_Saved() {
        Tag tagWithNullId = new Tag("tag with null id");
        Tag tagWithNotNullId = tagDAO.save(new Tag("tag with not null id"));

        Suit suit = new Suit("name", "desc", 4, null,
            Sets.newHashSet(tagWithNotNullId, tagWithNullId), null);
        Suit savedSuit = suitDAO.save(suit);

        suit.setId(savedSuit.getId());

        Assert.assertEquals(suit, savedSuit);
    }

    @Test
    public void save_SuitWithNotNewCase_Saved() {
        Tag tag = tagDAO.save(new Tag("tag"));

        Case caze = new Case();
        caze.addTag(tag);

        Suit suit = new Suit("name", "desc", 4, null, null,
            Lists.newArrayList(caze));

        Suit savedSuit = suitDAO.save(suit);

        suit.setId(savedSuit.getId());
        suit.getCases().get(0).setId(savedSuit.getCases().get(0).getId());

        Assert.assertEquals(suit, savedSuit);
    }

    @Test
    public void save_SuitsWithMultipleRepresentationOfSameTag_Saved() {
        Tag tag = tagDAO.save(new Tag("tag"));

        List<Suit> suits = retrieveSuiteList();
        for (Suit suit : suits) {
            suit.getTags().add(tag);
        }

        List<Suit> savedSuits = suitDAO.save(suits);

        Assert.assertEquals(suits.size(), savedSuits.size());

        for (int i = 0; i < suits.size(); ++i) {
            suits.get(i).setId(savedSuits.get(i).getId());
        }

        Assert.assertEquals(suits, savedSuits);
    }

    @Test
    public void testUpdatePriority() {
        final Suit originalSuit = retriveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setPriority(4);
        id = suitDAO.save(originalSuit).getId();

        final Suit newSuit = retriveSuit();

        final Set<Tag> tagsWithIds = suitDAO.findOne(id).getTags();
        final Set<Tag> tagsWithoutIds = newSuit.getTags();

        Assert.assertEquals(tagsWithIds.size(), tagsWithoutIds.size());

        final Set<Tag> unsavedTagsWithIds = setIdsForTags(tagsWithIds, tagsWithoutIds);

        newSuit.setId(id);
        newSuit.setPriority(4);
        newSuit.setTags(unsavedTagsWithIds);

        Assert.assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    public void testUpdateDescription() {
        final Suit originalSuit = retriveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setDescription("modified description");
        id = suitDAO.save(originalSuit).getId();

        final Suit newSuit = retriveSuit();

        final Set<Tag> tagsWithIds = suitDAO.findOne(id).getTags();
        final Set<Tag> tagsWithoutIds = newSuit.getTags();

        Assert.assertEquals(tagsWithIds.size(), tagsWithoutIds.size());

        final Set<Tag> unsavedTagsWithIds = setIdsForTags(tagsWithIds, tagsWithoutIds);

        newSuit.setId(id);
        newSuit.setDescription("modified description");
        newSuit.setTags(unsavedTagsWithIds);

        Assert.assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    public void testUpdateTags() {
        Suit savedSuit = suitDAO.save(retriveSuit());
        final Set<Tag> tags = retrieveTagList("tag1", "tag2", "tag3");
        savedSuit.setTags(tags);

        final Suit newSuit = retriveSuit();
        newSuit.setId(savedSuit.getId());
        newSuit.setTags(tags);

        Assert.assertEquals(newSuit, suitDAO.getOne(savedSuit.getId()));
    }

    @Test
    public void testUpdateName() {
        final Suit originalSuit = retriveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setName("Suit4a");
        id = suitDAO.save(originalSuit).getId();

        final Suit newSuit = retriveSuit();

        final Set<Tag> tagsWithIds = suitDAO.findOne(id).getTags();
        final Set<Tag> tagsWithoutIds = newSuit.getTags();

        Assert.assertEquals(tagsWithIds.size(), tagsWithoutIds.size());

        final Set<Tag> unsavedTagsWithIds = setIdsForTags(tagsWithIds, tagsWithoutIds);

        newSuit.setId(id);
        newSuit.setName("Suit4a");
        newSuit.setTags(unsavedTagsWithIds);

        Assert.assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    public void testRemoveById() {
        Suit originalSuit = retriveSuit();
        long id = suitDAO.save(originalSuit).getId();
        suitDAO.delete(id);

        Assert.assertTrue(!suitDAO.exists(id));
    }

    @Test
    public void testRemove() {
        Suit savedSuit = suitDAO.save(retriveSuit());
        suitDAO.delete(savedSuit);

        Assert.assertTrue(!suitDAO.exists(savedSuit.getId()));
    }

    @Test
    public void testAddList() {
        final List<Suit> savedSuits = suitDAO.save(retrieveSuiteList());

        final List<Suit> newSuits = retrieveSuiteList();

        Assert.assertEquals(newSuits.size(), savedSuits.size());

        fillIdsForListOfSuits(savedSuits, newSuits);

        List<Suit> findSuits = suitDAO.findAll();

        Assert.assertTrue(newSuits.equals(findSuits));
    }

    @Test
    public void testRemoveList() {
        List<Suit> savedSuits = suitDAO.save(retrieveSuiteList());

        suitDAO.delete(savedSuits);

        Assert.assertTrue(suitDAO.findAll().isEmpty());
    }

    private Suit retriveSuit() {
        final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return new Suit("Suit1", "Suit1 description", 3,
            Calendar.getInstance().getTime(), retrieveTagList("tag1", "tag2"), new ArrayList<>());
    }

    private List<Suit> retrieveSuiteList() {
        final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        final Suit suit1 = new Suit("Suit1", "Suit1 description", 5,
            Calendar.getInstance().getTime(), retrieveTagList("tag1", "tag2"), new ArrayList<>());

        final Suit suit2 = new Suit("Suit2", "Suit2 description", 5,
            Calendar.getInstance().getTime(), retrieveTagList("tag1"), new ArrayList<>());

        final Suit suit3 = new Suit("Suit3", "Suit3 description", 5,
            Calendar.getInstance().getTime(), retrieveTagList("tag1", "tag3"), new ArrayList<>());

        final List<Suit> suits = new ArrayList<>();
        suits.add(suit1);
        suits.add(suit2);
        suits.add(suit3);
        return suits;
    }

    private Set<Tag> retrieveTagList(String... tags) {
        return Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
    }

    private Set<Tag> setIdsForTags(Set<Tag> tagsWithIds, Set<Tag> tagsWithoutIds) {
        final List<Tag> listOfTagsWithIds = new ArrayList<>(tagsWithIds);
        final List<Tag> listOfTagsWithoutIds = new ArrayList<>(tagsWithoutIds);
        for (int i = 0; i < listOfTagsWithIds.size(); i++) {
            final Tag tagWithId = listOfTagsWithIds.get(i);
            listOfTagsWithoutIds.get(i).setId(tagWithId.getId());
        }
        return new HashSet<>(listOfTagsWithoutIds);
    }

    private void fillIdsForListOfSuits(List<Suit> savedSuits, List<Suit> newSuits) {
        for (int i = 0; i < savedSuits.size(); i++) {
            final Set<Tag> savedTags = savedSuits.get(i).getTags();
            final Set<Tag> tagsWithoutIds = newSuits.get(i).getTags();
            final Set<Tag> unsavedTagsWithIds = setIdsForTags(savedTags, tagsWithoutIds);
            newSuits.get(i).setId(savedSuits.get(i).getId());
            newSuits.get(i).setTags(unsavedTagsWithIds);
        }
    }
}
