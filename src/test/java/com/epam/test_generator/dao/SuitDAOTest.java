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

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={DatabaseConfigForTests.class})
@Transactional
public class SuitDAOTest {

    @Autowired
    SuitDAO suitDAO;

    @Test
    public void testCreateAndRetrieve() {
        Suit originalSuit = new Suit("Suit1", "Suit1 description");
        originalSuit.setPriority(3);
        originalSuit.setTags("tag1,tag2");
        long id = suitDAO.save(originalSuit).getId();
        Suit loadedSuit = suitDAO.getOne(id);

        assertSuits(originalSuit, loadedSuit);

        originalSuit.setPriority(4);
        id = suitDAO.save(originalSuit).getId();
        loadedSuit = suitDAO.getOne(id);

        assertSuits(originalSuit, loadedSuit);

        originalSuit.setDescription("Suit1 modified description");
        id = suitDAO.save(originalSuit).getId();
        loadedSuit = suitDAO.getOne(id);

        assertSuits(originalSuit, loadedSuit);

    }

    private void assertSuits(Suit originalSuit, Suit loadedSuit) {
        Assert.assertEquals(originalSuit.getId(), loadedSuit.getId());
        Assert.assertEquals(originalSuit.getName(), loadedSuit.getName());
        Assert.assertEquals(originalSuit.getDescription(), loadedSuit.getDescription());
        Assert.assertEquals(originalSuit.getPriority(), loadedSuit.getPriority());
        Assert.assertEquals(originalSuit.getCreationDate(), loadedSuit.getCreationDate());
        Assert.assertEquals(originalSuit.getTags(), loadedSuit.getTags());
    }
}
