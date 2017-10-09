package com.epam.test_generator.dao;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.entities.Suit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={DatabaseConfigForTests.class})
@Transactional
public class CaseDAOTest {

    @Autowired
    SuitDAO suitDAO;

    @Test
    public void testCreateAndRetrieve() {

        System.out.println("Hello there!");
        Suit suit = new Suit();
        suit.setDescription("desc");

        suit.setName("name");

        suit = suitDAO.save(suit);

        System.out.println(suit);
        Assert.isTrue(true);
    }
}