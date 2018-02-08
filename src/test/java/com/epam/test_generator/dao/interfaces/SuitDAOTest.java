package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class SuitDAOTest {

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private TagDAO tagDAO;


    @Test
    public void createAndRetrieve_Suit_Success() {
        final Suit originalSuit = retrieveSuit();
        final long id = suitDAO.save(originalSuit).getId();

        final Suit newSuit = retrieveSuit();

        final Set<Tag> tagsWithIds = suitDAO.findOne(id).getTags();
        final Set<Tag> tagsWithoutIds = newSuit.getTags();

        assertEquals(tagsWithIds.size(), tagsWithoutIds.size());

        final Set<Tag> unsavedTagsWithIds = setIdsForTags(tagsWithIds, tagsWithoutIds);

        newSuit.setId(id);
        newSuit.setTags(unsavedTagsWithIds);

        assertEquals(newSuit, suitDAO.findOne(id));
    }

    @Test
    public void save_SuitWithNullIdAndNotNullTagId_Saved() {
        Tag tagWithNullId = new Tag("tag with null id");
        Tag tagWithNotNullId = tagDAO.save(new Tag("tag with not null id"));

        Suit suit = new Suit("name", "desc", 4, null,
            Sets.newHashSet(tagWithNotNullId, tagWithNullId), null, 1);
        Suit savedSuit = suitDAO.save(suit);

        suit.setId(savedSuit.getId());

        assertEquals(suit, savedSuit);
    }

    @Test
    public void save_SuitWithNotNewCase_Saved() {
        Tag tag = tagDAO.save(new Tag("tag"));

        Case caze = new Case();
        caze.addTag(tag);

        Suit suit = new Suit("name", "desc", 4, null, null,
            Lists.newArrayList(caze), 1);

        Suit savedSuit = suitDAO.save(suit);

        suit.setId(savedSuit.getId());
        suit.getCases().get(0).setId(savedSuit.getCases().get(0).getId());

        assertEquals(suit, savedSuit);
    }

    @Test
    public void save_SuitsWithMultipleRepresentationOfSameTag_Saved() {
        Tag tag = tagDAO.save(new Tag("tag"));

        List<Suit> suits = retrieveSuiteList();
        for (Suit suit : suits) {
            suit.getTags().add(tag);
        }

        List<Suit> savedSuits = suitDAO.save(suits);

        assertEquals(suits.size(), savedSuits.size());

        for (int i = 0; i < suits.size(); ++i) {
            suits.get(i).setId(savedSuits.get(i).getId());
        }

        assertEquals(suits, savedSuits);
    }

    @Test
    public void updatePriority_Suit_Success() {
        final Suit originalSuit = retrieveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setPriority(4);
        id = suitDAO.save(originalSuit).getId();

        final Suit newSuit = retrieveSuit();

        final Set<Tag> tagsWithIds = suitDAO.findOne(id).getTags();
        final Set<Tag> tagsWithoutIds = newSuit.getTags();

        assertEquals(tagsWithIds.size(), tagsWithoutIds.size());

        final Set<Tag> unsavedTagsWithIds = setIdsForTags(tagsWithIds, tagsWithoutIds);

        newSuit.setId(id);
        newSuit.setPriority(4);
        newSuit.setTags(unsavedTagsWithIds);

        assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    public void updateDescription_Suit_Success() {
        final Suit originalSuit = retrieveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setDescription("modified description");
        id = suitDAO.save(originalSuit).getId();

        final Suit newSuit = retrieveSuit();

        final Set<Tag> tagsWithIds = suitDAO.findOne(id).getTags();
        final Set<Tag> tagsWithoutIds = newSuit.getTags();

        assertEquals(tagsWithIds.size(), tagsWithoutIds.size());

        final Set<Tag> unsavedTagsWithIds = setIdsForTags(tagsWithIds, tagsWithoutIds);

        newSuit.setId(id);
        newSuit.setDescription("modified description");
        newSuit.setTags(unsavedTagsWithIds);

        assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    public void updateTags_Suit_Success() {
        final Suit originalSuit = retrieveSuit();
        Suit savedSuit = suitDAO.save(originalSuit);
        final Set<Tag> tags = retrieveTagList("tag1", "tag2", "tag3");
        savedSuit.setTags(tags);

        final Suit newSuit = retrieveSuit();
        newSuit.setId(savedSuit.getId());
        newSuit.setTags(tags);

        assertEquals(newSuit, suitDAO.getOne(savedSuit.getId()));
    }

    @Test
    public void updateName_Suit_Success() {
        final Suit originalSuit = retrieveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setName("Suit4a");
        id = suitDAO.save(originalSuit).getId();

        final Suit newSuit = retrieveSuit();

        final Set<Tag> tagsWithIds = suitDAO.findOne(id).getTags();
        final Set<Tag> tagsWithoutIds = newSuit.getTags();

        assertEquals(tagsWithIds.size(), tagsWithoutIds.size());

        final Set<Tag> unsavedTagsWithIds = setIdsForTags(tagsWithIds, tagsWithoutIds);

        newSuit.setId(id);
        newSuit.setName("Suit4a");
        newSuit.setTags(unsavedTagsWithIds);

        assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void updateRowNumber_Suit_Success() {

        suitDAO.save(Arrays.asList(
            new Suit(null, "1", "1", new ArrayList<>(), 1, new HashSet<>(), 1),
            new Suit(null, "2", "2", new ArrayList<>(), 2, new HashSet<>(), 2),
            new Suit(null, "3", "3", new ArrayList<>(), 3, new HashSet<>(), 3)
        ));

        final Suit suit1 = suitDAO.findOne(1L);
        suit1.setRowNumber(3);
        final Suit suit2 = suitDAO.findOne(2L);
        suit2.setRowNumber(1);
        final Suit suit3 = suitDAO.findOne(3L);
        suit3.setRowNumber(2);

        suitDAO.save(Arrays.asList(suit1, suit2, suit3));

        assertThat(3, is(equalTo(suitDAO.findOne(1L).getRowNumber())));
        assertThat(1, is(equalTo(suitDAO.findOne(2L).getRowNumber())));
        assertThat(2, is(equalTo(suitDAO.findOne(3L).getRowNumber())));
    }

    @Test
    public void removeById_Suit_Success() {
        Suit originalSuit = retrieveSuit();
        long id = suitDAO.save(originalSuit).getId();
        suitDAO.delete(id);

        Assert.assertTrue(!suitDAO.exists(id));
    }

    @Test
    public void remove_Suit_Success() {
        Suit originalSuit = retrieveSuit();
        Suit savedSuit = suitDAO.save(originalSuit);
        suitDAO.delete(originalSuit);

        Assert.assertTrue(!suitDAO.exists(savedSuit.getId()));
    }

    @Test
    public void addList_Suits_Success() {
        final List<Suit> savedSuits = suitDAO.save(retrieveSuiteList());

        final List<Suit> newSuits = retrieveSuiteList();

        assertEquals(newSuits.size(), savedSuits.size());

        fillIdsForListOfSuits(savedSuits, newSuits);

        List<Suit> findSuits = suitDAO.findAll();

        Assert.assertTrue(newSuits.equals(findSuits));
    }

    @Test
    public void removeList_Suits_Success() {
        List<Suit> savedSuits = suitDAO.save(retrieveSuiteList());

        suitDAO.delete(savedSuits);

        Assert.assertTrue(suitDAO.findAll().isEmpty());
    }

    @After
    public void tearDown() {
        suitDAO.deleteAll();
    }

    private Suit retrieveSuit() {
        return new Suit(
            "Suit1",
            "Suit1 description",
            3,
            Calendar.getInstance().getTime(),
            retrieveTagList("tag1", "tag2"),
            new ArrayList<>(),
            1);
    }

    private List<Suit> retrieveSuiteList() {
        final Suit suit1 = new Suit(
            "Suit1",
            "Suit1 description",
            5,
            Calendar.getInstance().getTime(),
            retrieveTagList("tag1", "tag2"),
            new ArrayList<>(),
            1);

        final Suit suit2 = new Suit("Suit2", "Suit2 description", 5,
            Calendar.getInstance().getTime(), retrieveTagList("tag1"), new ArrayList<>(), 1);

        final Suit suit3 = new Suit("Suit3", "Suit3 description", 5,
            Calendar.getInstance().getTime(),
            retrieveTagList("tag1", "tag3"), new ArrayList<>(), 1);

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
