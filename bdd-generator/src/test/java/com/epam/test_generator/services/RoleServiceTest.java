package com.epam.test_generator.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.RoleDAO;
import com.epam.test_generator.entities.Role;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;


@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {


    @Mock
    private RoleDAO roleDAO;

    @Mock
    private Environment environment;

    @InjectMocks
    private RoleService sut;


    private final List<Role> availableRoles = new ArrayList<>();
    private final static String ROLE_NAME = "ADMIN";
    private final Role adminRole = new Role(ROLE_NAME);
    private final static String ROLES = "ADMIN, TEST_ENGINEER, TEST_LEAD, GUEST";

    @Before
    public void setUp() {
        fillAllAvailableRoles(availableRoles);
    }

    @Test
    public void getRoleByName_ValidName_Ok() {
        when(roleDAO.findByName(ROLE_NAME)).thenReturn(adminRole);

        Role retrievedRoleByName = sut.getRoleByName(ROLE_NAME);

        Assert.assertThat(retrievedRoleByName, is(equalTo(adminRole)));
    }

    @Test
    public void addRole_ValidName_Ok() {
        sut.addRole(adminRole);
        verify(roleDAO).save(adminRole);
    }

    @Test
    public void findAll_SimpleRoles_Ok() {
        when(roleDAO.findAll()).thenReturn(availableRoles);
        List<Role> expectedRoles = sut.findAll();
        Assert.assertEquals(expectedRoles, availableRoles);
    }

    @Test
    public void getRolesFromProperties_SimpleRoles_Ok() {
        when(environment.getProperty(anyString())).thenReturn(ROLES);

        List<Role> allRolesFromProperties = sut.getRolesFromProperties();

        Assert.assertEquals(allRolesFromProperties, availableRoles);
    }

    private void fillAllAvailableRoles(List<Role> availableRoles) {
        List<String> possibleRolesNames = Arrays
            .asList("ADMIN", "TEST_ENGINEER", "TEST_LEAD", "GUEST");
        possibleRolesNames.forEach(rn -> availableRoles.add(new Role(rn)));
    }
}