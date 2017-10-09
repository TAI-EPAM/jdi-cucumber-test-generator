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

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={DatabaseConfigForTests.class})
@Transactional
public class SuitDAOTest {

    @Autowired
    SuitDAO suitDAO;

    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    @Test
    public void testCreateAndRetrieve() {
        Suit originalSuit = new Suit("Suit1", "Suit1 description");
        originalSuit.setPriority(3);
        originalSuit.setTags("tag1,tag2");
        originalSuit.setCases(new ArrayList<>());
        originalSuit.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        long id = suitDAO.save(originalSuit).getId();

        Assert.assertEquals(originalSuit, suitDAO.findOne(id));
    }

//    @Test
//    public void testCreateRetrieveAndModify() {
//        Suit originalSuit = new Suit("Suit1", "Suit1 description");
//
//        originalSuit.setPriority(3);
//        originalSuit.setTags("tag1,tag2");
//        originalSuit.setCases(new ArrayList<>());
//        originalSuit.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
//
//        long id = suitDAO.save(originalSuit).getId();
//
//        Suit loadedSuit = suitDAO.findOne(id);
//
//        loadedSuit.setDescription("modified description");
//
//        System.out.println(originalSuit);
//        System.out.println(loadedSuit);
//
//        Assert.assertNotEquals(originalSuit, loadedSuit);
//    }

    @Test
    public void testUpdatePriority() {
        Suit originalSuit = new Suit("Suit2", "Suit2 description");
        originalSuit.setPriority(3);
        originalSuit.setTags("tag1 tag2");
        originalSuit.setCases(new ArrayList<>());
        originalSuit.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setPriority(4);
        id = suitDAO.save(originalSuit).getId();

        Assert.assertEquals(originalSuit, suitDAO.getOne(id));
    }

    @Test
    public void testUpdateDescription() {
        Suit originalSuit = new Suit("Suit3", "Suit3 description");
        originalSuit.setPriority(3);
        originalSuit.setTags("tag1 tag2");
        originalSuit.setCases(new ArrayList<>());
        originalSuit.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setDescription("modified description");
        id = suitDAO.save(originalSuit).getId();

        Assert.assertEquals(originalSuit, suitDAO.getOne(id));
    }

    @Test
    public void testUpdateTags() {
        Suit originalSuit = new Suit("Suit4", "Suit4 description");
        originalSuit.setPriority(5);
        originalSuit.setTags("tag1 tag2");
        originalSuit.setCases(new ArrayList<>());
        originalSuit.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setTags("tag1 tag2 tag3");
        id = suitDAO.save(originalSuit).getId();

        Assert.assertEquals(originalSuit, suitDAO.getOne(id));
    }

    @Test
    public void testUpdateName() {
        Suit originalSuit = new Suit("Suit4", "Suit4 description");
        originalSuit.setPriority(5);
        originalSuit.setTags("tag1 tag2");
        originalSuit.setCases(new ArrayList<>());
        originalSuit.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        long id = suitDAO.save(originalSuit).getId();
        originalSuit.setId(id);
        originalSuit.setName("Suit4a");
        id = suitDAO.save(originalSuit).getId();

        Assert.assertEquals(originalSuit, suitDAO.getOne(id));
    }

    @Test
    public void testRemoveById() {
        Suit originalSuit = new Suit("Suit4", "Suit4 description");
        originalSuit.setPriority(5);
        originalSuit.setTags("tag1 tag2");
        originalSuit.setCases(new ArrayList<>());
        originalSuit.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        long id = suitDAO.save(originalSuit).getId();
        suitDAO.delete(id);

        Assert.assertTrue(!suitDAO.exists(id));
    }

    @Test
    public void testRemove() {
        Suit originalSuit = new Suit("Suit4", "Suit4 description");
        originalSuit.setPriority(5);
        originalSuit.setTags("tag1 tag2");
        originalSuit.setCases(new ArrayList<>());
        originalSuit.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        long id = suitDAO.save(originalSuit).getId();
        suitDAO.delete(originalSuit);

        Assert.assertTrue(!suitDAO.exists(id));
    }

    @Test
    public void testAddList() {
        Suit suit1 = new Suit("Suit1", "Suit1 description");
        suit1.setPriority(5);
        suit1.setTags("tag1 tag2");
        suit1.setCases(new ArrayList<>());
        suit1.setCreationDate(formatter.format(Calendar.getInstance().getTime()));

        Suit suit2 = new Suit("Suit2", "Suit2 description");
        suit2.setPriority(5);
        suit2.setTags("tag1");
        suit2.setCases(new ArrayList<>());
        suit2.setCreationDate(formatter.format(Calendar.getInstance().getTime()));

        Suit suit3 = new Suit("Suit3", "Suit3 description");
        suit3.setPriority(5);
        suit3.setTags("tag1 tag3");
        suit3.setCases(new ArrayList<>());
        suit3.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        Suit[] suits = {suit1, suit2, suit3};

        suitDAO.save(Arrays.asList(suits));

        Assert.assertEquals(suitDAO.findAll(), Arrays.asList(suits));
    }

    @Test
    public void testRemoveList() {
        Suit suit1 = new Suit("Suit1", "Suit1 description");
        suit1.setPriority(5);
        suit1.setTags("tag1 tag2");
        suit1.setCases(new ArrayList<>());
        suit1.setCreationDate(formatter.format(Calendar.getInstance().getTime()));

        Suit suit2 = new Suit("Suit2", "Suit2 description");
        suit2.setPriority(5);
        suit2.setTags("tag1");
        suit2.setCases(new ArrayList<>());
        suit2.setCreationDate(formatter.format(Calendar.getInstance().getTime()));

        Suit suit3 = new Suit("Suit3", "Suit3 description");
        suit3.setPriority(5);
        suit3.setTags("tag1 tag3");
        suit3.setCases(new ArrayList<>());
        suit3.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
        Suit[] suits = {suit1, suit2, suit3};

        suitDAO.save(Arrays.asList(suits));

        suitDAO.delete(Arrays.asList(suits));

        Assert.assertTrue(suitDAO.findAll().isEmpty());
    }
}
