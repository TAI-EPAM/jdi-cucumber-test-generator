package com.epam.test_generator.dao.interfaces;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class SuitDAOTest {

    @Autowired
    private SuitDAO suitDAO;

    @Test
    public void createAndRetrieve_Suit_Success() {
        Suit originalSuit = retrieveSuit();

        long id = suitDAO.save(originalSuit).getId();

        Suit newSuit = retrieveSuit();

        Optional<Suit> byIdOptional = suitDAO.findById(id);

        assertTrue(byIdOptional.isPresent());

        Set<Tag> tagsWithIds = byIdOptional.get().getTags();

        newSuit.setId(id);
        newSuit.setTags(tagsWithIds);

        assertEquals(newSuit, byIdOptional.get());
    }

    @Test
    public void save_SuitWithTag_Saved() {
        Tag tag = new Tag("tag with null id");

        Suit suit = new Suit("name", "desc", 4, null, null,
            Sets.newHashSet(tag), null, 1);
        Suit savedSuit = suitDAO.save(suit);

        suit.setId(savedSuit.getId());

        assertEquals(suit, savedSuit);
    }

    @Test
    public void save_SuitWithNotNewCase_Saved() {
        Tag tag = new Tag("tag");

        Case caze = new Case();
        caze.setName("test");
        caze.addTag(tag);
        caze.setRowNumber(1);

        Suit suit = new Suit("name", "desc", 4, null, null, null,
            Lists.newArrayList(caze), 1);

        Suit savedSuit = suitDAO.save(suit);

        suit.setId(savedSuit.getId());
        suit.getCases().get(0).setId(savedSuit.getCases().get(0).getId());

        assertEquals(suit, savedSuit);
    }

    @Test
    public void save_SuitsWithMultipleRepresentationOfSameTag_Saved() {
        Tag tag = new Tag("tag");

        List<Suit> suits = retrieveSuiteList();
        for (Suit suit : suits) {
            suit.getTags().add(tag);
        }

        List<Suit> savedSuits = suitDAO.saveAll(suits);

        assertEquals(suits.size(), savedSuits.size());

        for (int i = 0; i < suits.size(); ++i) {
            suits.get(i).setId(savedSuits.get(i).getId());
        }

        assertEquals(suits, savedSuits);
    }

    @Test
    public void updatePriority_Suit_Success() {
        Suit originalSuit = retrieveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setPriority(4);
        id = suitDAO.save(originalSuit).getId();

        Suit newSuit = retrieveSuit();

        Optional<Suit> byIdOprtional = suitDAO.findById(id);

        assertTrue(byIdOprtional.isPresent());
        Set<Tag> tags = byIdOprtional.get().getTags();

        newSuit.setId(id);
        newSuit.setPriority(4);

        newSuit.setTags(tags);

        assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    public void updateDescription_Suit_Success() {
        Suit originalSuit = retrieveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setDescription("modified description");
        id = suitDAO.save(originalSuit).getId();

        Suit newSuit = retrieveSuit();

        Optional<Suit> byIdOptional = suitDAO.findById(id);

        assertTrue(byIdOptional.isPresent());

        Set<Tag> tags = byIdOptional.get().getTags();

        newSuit.setId(id);
        newSuit.setDescription("modified description");
        newSuit.setTags(tags);

        assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    public void updateTags_Suit_Success() {
        Suit originalSuit = retrieveSuit();
        Suit savedSuit = suitDAO.save(originalSuit);
        Set<Tag> tags = retrieveTagList("tag1", "tag2", "tag3");
        savedSuit.setTags(tags);

        Suit newSuit = retrieveSuit();
        newSuit.setId(savedSuit.getId());
        newSuit.setTags(tags);

        assertEquals(newSuit, suitDAO.getOne(savedSuit.getId()));
    }

    @Test
    public void updateName_Suit_Success() {
        Suit originalSuit = retrieveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setName("Suit4a");
        id = suitDAO.save(originalSuit).getId();

        Suit newSuit = retrieveSuit();

        Optional<Suit> byIdOptional = suitDAO.findById(id);

        assertTrue(byIdOptional.isPresent());

        Set<Tag> tags = byIdOptional.get().getTags();

        newSuit.setId(id);
        newSuit.setName("Suit4a");

        newSuit.setTags(tags);

        assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void updateRowNumber_Suit_Success() {

        ArrayList<Suit> suits = new ArrayList<>(Arrays.asList(
            new Suit(null, "1", "1", new ArrayList<>(), 1, new HashSet<>(), 1),
            new Suit(null, "2", "2", new ArrayList<>(), 2, new HashSet<>(), 2),
            new Suit(null, "3", "3", new ArrayList<>(), 3, new HashSet<>(), 3)
        ));
        suitDAO.saveAll(suits);

        Optional<Suit> byIdOptional1 = suitDAO.findById(suits.get(0).getId());

        assertTrue(byIdOptional1.isPresent());

        Suit suit1 = byIdOptional1.get();
        suit1.setRowNumber(3);

        Optional<Suit> byIdOptional2 = suitDAO.findById(suits.get(1).getId());
        assertTrue(byIdOptional2.isPresent());

        Suit suit2 = byIdOptional2.get();

        suit2.setRowNumber(1);
        Optional<Suit> byIdOptional3 = suitDAO.findById(suits.get(2).getId());

        assertTrue(byIdOptional3.isPresent());

        Suit suit3 = byIdOptional3.get();
        suit3.setRowNumber(2);

        suitDAO.saveAll(Arrays.asList(suit1, suit2, suit3));

        assertThat(3,
            is(equalTo(suitDAO.findById(suits.get(0).getId()).orElse(suit1).getRowNumber())));
        assertThat(1,
            is(equalTo(suitDAO.findById(suits.get(1).getId()).orElse(suit2).getRowNumber())));
        assertThat(2, is(equalTo(suitDAO.findById(suits.get(2).getId()).orElse(suit3).getRowNumber()
        )));
    }

    @Test
    public void removeById_Suit_Success() {
        Suit originalSuit = retrieveSuit();
        long id = suitDAO.save(originalSuit).getId();
        suitDAO.deleteById(id);

        Assert.assertFalse(suitDAO.existsById(id));
    }

    @Test
    public void remove_Suit_Success() {
        Suit originalSuit = retrieveSuit();
        Suit savedSuit = suitDAO.save(originalSuit);
        suitDAO.delete(originalSuit);

        Assert.assertFalse(suitDAO.existsById(savedSuit.getId()));
    }

    @Test
    public void addList_Suits_Success() {
        List<Suit> savedSuits = suitDAO.saveAll(retrieveSuiteList());

        List<Suit> newSuits = retrieveSuiteList();

        assertEquals(newSuits.size(), savedSuits.size());

        fillIdsForListOfSuits(savedSuits, newSuits);

        List<Suit> findSuits = suitDAO.findAll();

        Assert.assertEquals(newSuits, findSuits);
    }

    @Test
    public void removeList_Suits_Success() {
        List<Suit> savedSuits = suitDAO.saveAll(retrieveSuiteList());

        suitDAO.deleteAll(savedSuits);

        Assert.assertTrue(suitDAO.findAll().isEmpty());
    }

    private Suit retrieveSuit() {
        return new Suit(
            "Suit1",
            "Suit1 description",
            3,
            ZonedDateTime.now(),
            ZonedDateTime.now(),
            retrieveTagList("tag1", "tag2"),
            new ArrayList<>(),
            1);
    }

    private List<Suit> retrieveSuiteList() {
        Suit suit1 = new Suit(
            "Suit1",
            "Suit1 description",
            5,
            ZonedDateTime.now(),
            ZonedDateTime.now(),
            retrieveTagList("tag1", "tag2"),
            new ArrayList<>(),
            1);

        Suit suit2 = new Suit("Suit2", "Suit2 description", 5,
            ZonedDateTime.now(), ZonedDateTime.now(),
            retrieveTagList("tag1"), new ArrayList<>(), 1);

        Suit suit3 = new Suit("Suit3", "Suit3 description", 5,
            ZonedDateTime.now(), ZonedDateTime.now(),
            retrieveTagList("tag1", "tag3"), new ArrayList<>(), 1);

        List<Suit> suits = new ArrayList<>();
        suits.add(suit1);
        suits.add(suit2);
        suits.add(suit3);
        return suits;
    }

    private Set<Tag> retrieveTagList(String... tags) {
        return Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
    }


    private void fillIdsForListOfSuits(List<Suit> savedSuits, List<Suit> newSuits) {
        for (int i = 0; i < savedSuits.size(); i++) {
            Set<Tag> tags = newSuits.get(i).getTags();
            newSuits.get(i).setId(savedSuits.get(i).getId());
            newSuits.get(i).setTags(tags);
        }
    }
}
