package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;


    private final User aUser = new User();


    @Before
    public void setUp() {
        aUser.setName("John");
        aUser.setSurname("Doe");
        aUser.setEmail("John_Doe@epam.com");
        aUser.setPassword("admin");
        aUser.setRole(new Role("ADMIN"));
    }


    @Test
    public void testSave_UpdateName_nameWasUpdatedSuccessfully() {

        userDAO.save(aUser);


        final User savedAUser = userDAO.findByEmail(aUser.getEmail());

        assertNotNull(savedAUser);

        final String currentName = savedAUser.getName();
        assertEquals(currentName, aUser.getName());

        aUser.setName("new Name");
        userDAO.save(aUser);

        final User aUserWithUpdatedName = userDAO.findById(savedAUser.getId());

        assertNotNull(aUserWithUpdatedName);

        final String newName = aUserWithUpdatedName.getName();
        final String oldName = aUser.getName();
        assertEquals(newName, oldName);

    }


    @Test
    public void testSave_UpdateSurname_surnameWasUpdatedSuccessfully() {
        userDAO.save(aUser);


        final User savedAUser = userDAO.findByEmail(aUser.getEmail());

        assertNotNull(savedAUser);

        final String currentSurname = savedAUser.getSurname();
        assertEquals(currentSurname, aUser.getSurname());

        aUser.setSurname("new Surname");
        userDAO.save(aUser);

        final User aUserWithUpdatedSurname = userDAO.findById(savedAUser.getId());

        assertNotNull(aUserWithUpdatedSurname);

        final String newSurname = aUserWithUpdatedSurname.getSurname();
        final String oldSurname = aUser.getSurname();
        assertEquals(newSurname, oldSurname);
    }

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
    public void findAll_test() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new User());
        }
        userDAO.save(users);
        assertEquals(users.size(), userDAO.findAll().size());
    }


    @After
    public void tearDown(){
        userDAO.delete(aUser);
    }

}