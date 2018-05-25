package com.epam.test_generator.dao.interfaces;


import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.RoleDAO;
import com.epam.test_generator.entities.Role;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class RoleDAOTest {

    private final static String ROLE_NAME = "NEW_ROLE";

    @Autowired
    private RoleDAO roleDAO;


    @Test
    public void save_SimpleRole_SaveAnInstance() {
        int expectedSize = roleDAO.findAll().size() + 1;

        Role someRole = new Role();
        roleDAO.save(someRole);

        Assert.assertEquals(expectedSize, roleDAO.findAll().size());
    }


    @Test
    public void findByName_SimpleRole_ReturnExpectedRole() {

        Role aValidRole = new Role(ROLE_NAME);

        roleDAO.save(aValidRole);

        Role retrievedRoleByName = roleDAO.findByName(ROLE_NAME);
        Assert.assertThat(retrievedRoleByName, is(equalTo(aValidRole)));

    }

    @Test
    public void findByName_NullRole_ReturnsNullDueToAbsenceOfItemWithThisName() {

        Role byName = roleDAO.findByName(ROLE_NAME);
        Assert.assertThat(byName, is(nullValue()));
    }
}
