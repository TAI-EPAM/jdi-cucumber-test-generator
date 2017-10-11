package com.epam.test_generator.dao;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.entities.Suit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={DatabaseConfigForTests.class})
@Transactional
public class SuitDAOTest {

    @Autowired
    SuitDAO suitDAO;

    @Test
    public void testCreateAndRetrieve() {
        Suit originalSuit = retriveSuit();
        long id = suitDAO.save(originalSuit).getId();

        Suit newSuit = retriveSuit();
        newSuit.setId(id);

        Assert.assertEquals(newSuit, suitDAO.findOne(id));
    }

    @Test
    public void testUpdatePriority() {
        Suit originalSuit = retriveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setPriority(4);
        id = suitDAO.save(originalSuit).getId();

        Suit newSuit = retriveSuit();
        newSuit.setId(id);
        newSuit.setPriority(4);

        Assert.assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    public void testUpdateDescription() {
        Suit originalSuit = retriveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setDescription("modified description");
        id = suitDAO.save(originalSuit).getId();

        Suit newSuit = retriveSuit();
        newSuit.setId(id);
        newSuit.setDescription("modified description");

        Assert.assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    public void testUpdateTags() {
        Suit originalSuit = retriveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setTags("tag1 tag2 tag3");
        id = suitDAO.save(originalSuit).getId();

        Suit newSuit = retriveSuit();
        newSuit.setId(id);
        newSuit.setTags("tag1 tag2 tag3");

        Assert.assertEquals(newSuit, suitDAO.getOne(id));
    }

    @Test
    public void testUpdateName() {
        Suit originalSuit = retriveSuit();
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setName("Suit4a");
        id = suitDAO.save(originalSuit).getId();

        Suit newSuit = retriveSuit();
        newSuit.setId(id);
        newSuit.setName("Suit4a");

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
        Suit originalSuit = retriveSuit();
        long id = suitDAO.save(originalSuit).getId();
        suitDAO.delete(originalSuit);

        Assert.assertTrue(!suitDAO.exists(id));
    }

    @Test
    public void testAddList() {
        List<Suit> suits = retrieveSuiteList();

        List<Long> ids = suitDAO.save(suits).stream().map(Suit::getId).collect(Collectors.toList());

        List<Suit> newSuits = retrieveSuiteList();
        newSuits.get(0).setId(ids.get(0));
        newSuits.get(1).setId(ids.get(1));
        newSuits.get(2).setId(ids.get(2));

        Assert.assertTrue(newSuits.equals(suitDAO.findAll()));
    }

    @Test
    public void testRemoveList() {
        List<Suit> suits = retrieveSuiteList();

        suitDAO.save(suits);

        suitDAO.delete(suits);

        Assert.assertTrue(suitDAO.findAll().isEmpty());
    }

    private Suit retriveSuit() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return new Suit("Suit1", "Suit1 description", 3,
                formatter.format(Calendar.getInstance().getTime()), "tag1,tag2", new ArrayList<>());
    }

    private List<Suit> retrieveSuiteList() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Suit suit1 = new Suit("Suit1", "Suit1 description", 5,
                formatter.format(Calendar.getInstance().getTime()), "tag1 tag2", new ArrayList<>());

        Suit suit2 = new Suit("Suit2", "Suit2 description", 5,
                formatter.format(Calendar.getInstance().getTime()), "tag1 ", new ArrayList<>());

        Suit suit3 = new Suit("Suit3", "Suit3 description", 5,
                formatter.format(Calendar.getInstance().getTime()), "tag1 tag3", new ArrayList<>());

        ArrayList<Suit> suits = new ArrayList<>();
        suits.add(suit1);
        suits.add(suit2);
        suits.add(suit3);
        return suits;
    }
}
