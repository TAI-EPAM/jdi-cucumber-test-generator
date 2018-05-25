package com.epam.test_generator.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.admin.request.UserRoleUpdateDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.BadRoleException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {


    private final static String USER_EMAIL = "test@test.com";

    private final static String OLD_USER_ROLE = "GUEST";
    private final static String NEW_USER_ROLE = "ADMIN";

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private AdminService adminService;

    private UserRoleUpdateDTO userRoleDTO;

    private User user;

    private Role oldRole;

    private Role newRole;

    @Before
    public void setUp() {
        userRoleDTO = getUserDtoFor(USER_EMAIL, NEW_USER_ROLE);
        oldRole = getRoleFor(OLD_USER_ROLE);
        newRole = getRoleFor(NEW_USER_ROLE);
        user = getUserFor(USER_EMAIL, oldRole);
    }


    @Test
    public void change_UserRole_Success() {
        assertThat(user.getRole(), is(equalTo(oldRole)));

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(roleService.getRoleByName(anyString())).thenReturn(newRole);
        adminService.changeUserRole(userRoleDTO);

        assertThat(user.getRole(), is(equalTo(newRole)));
    }


    @Test(expected = BadRoleException.class)
    public void change_UserRole_Exception() {
        assertThat(user.getRole(), is(equalTo(oldRole)));

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(roleService.getRoleByName(anyString())).thenReturn(null);
        adminService.changeUserRole(userRoleDTO);

    }


    private Role getRoleFor(String userRole) {
        Role role = new Role();
        role.setName(userRole);
        return role;
    }

    private User getUserFor(String userEmail, Role userRole) {
        User user = new User();
        user.setEmail(userEmail);
        user.setRole(userRole);
        return user;
    }

    private UserRoleUpdateDTO getUserDtoFor(String userEmail, String userRole) {
        UserRoleUpdateDTO userRoleDTO = new UserRoleUpdateDTO();
        userRoleDTO.setEmail(userEmail);
        userRoleDTO.setRole(userRole);
        return userRoleDTO;
    }
}