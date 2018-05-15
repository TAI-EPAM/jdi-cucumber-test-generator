package com.epam.test_generator.dao.interfaces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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


        User savedAUser = userDAO.findByEmail(aUser.getEmail());

        assertNotNull(savedAUser);

        String currentName = savedAUser.getName();
        assertEquals(currentName, aUser.getName());

        aUser.setName("new Name");
        userDAO.save(aUser);

        Optional<User> aUserWithUpdatedNameOptional = userDAO.findById(savedAUser.getId());

        assertTrue(aUserWithUpdatedNameOptional.isPresent());

        String newName = aUserWithUpdatedNameOptional.get().getName();
        String oldName = aUser.getName();
        assertEquals(newName, oldName);

    }


    @Test
    public void testSave_UpdateSurname_surnameWasUpdatedSuccessfully() {
        userDAO.save(aUser);


        User savedAUser = userDAO.findByEmail(aUser.getEmail());

        assertNotNull(savedAUser);

        String currentSurname = savedAUser.getSurname();
        assertEquals(currentSurname, aUser.getSurname());

        aUser.setSurname("new Surname");
        userDAO.save(aUser);

        Optional<User> aUserWithUpdatedSurnameOptional = userDAO.findById(savedAUser.getId());

        assertTrue(aUserWithUpdatedSurnameOptional.isPresent());

        String newSurname = aUserWithUpdatedSurnameOptional.get().getSurname();
        String oldSurname = aUser.getSurname();
        assertEquals(newSurname, oldSurname);
    }

    @Test
    public void findByEmail_test() {
        String email = "test@email.ru";
        User user = new User();
        user.setEmail(email);

        userDAO.save(user);
        assertEquals(user, userDAO.findByEmail(email));
    }

    @Test
    public void findByID_test() {
        User user = new User();

        userDAO.save(user);
        assertEquals(user, userDAO.findById(user.getId()).orElse(null));
    }

    @Test
    public void findAll_test() {
        int previousSize = userDAO.findAll().size();
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new User());
        }
        userDAO.saveAll(users);
        assertEquals(users.size(), userDAO.findAll().size() - previousSize);
    }


    @After
    public void tearDown(){
        userDAO.delete(aUser);
    }

}