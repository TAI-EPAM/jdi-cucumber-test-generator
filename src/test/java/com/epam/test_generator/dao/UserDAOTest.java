package com.epam.test_generator.dao;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.entities.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class UserDAOTest {

    @Autowired
    UserDAO userDAO;


    @Test
    public void findByEmail_test() throws Exception {
        String email = "test@email.ru";
        User user = new User();
        user.setEmail(email);

        userDAO.save(user);
        assertEquals(user, userDAO.findByEmail(email));
    }

    @Test
    public void findByID_test() throws Exception {
        String email = "test@email.ru";
        User user = new User();

        userDAO.save(user);
        assertEquals(user, userDAO.findById(user.getId()));
    }

    @Test
    public void findAll_test() throws Exception {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new User());
        }

        userDAO.save(users);
        assertEquals(users.size(), userDAO.findAll().size());
    }

}