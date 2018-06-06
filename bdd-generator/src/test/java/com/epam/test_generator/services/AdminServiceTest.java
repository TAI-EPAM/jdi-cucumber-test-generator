package com.epam.test_generator.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.admin.request.UserRoleUpdateDTO;
import com.epam.test_generator.controllers.user.UserDTOsTransformer;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.BadRoleException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {


    private static final String USER_EMAIL = "test@test.com";

    private static final String OLD_USER_ROLE = "GUEST";
    private static final String NEW_USER_ROLE = "ADMIN";
    private static final String TEST_ENGINEER_ROLE = "TEST_ENGINEER";

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserDTOsTransformer userDTOsTransformer;

    @InjectMocks
    private AdminService adminService;

    private UserRoleUpdateDTO userRoleDTO;

    private UserRoleUpdateDTO userEngineerRoleDTO;

    private User user;

    private Role oldRole;

    private Role testEngineerRole;

    @Before
    public void setUp() {
        userRoleDTO = getUserDtoFor(USER_EMAIL, NEW_USER_ROLE);
        userEngineerRoleDTO = getUserDtoFor(USER_EMAIL, TEST_ENGINEER_ROLE);
        oldRole = getRoleFor(OLD_USER_ROLE);
        testEngineerRole = getRoleFor(TEST_ENGINEER_ROLE);
        user = getUserFor(USER_EMAIL, oldRole);
    }


    @Test( expected = BadRoleException.class)
    public void change_UserRole_To_Admin_Exception() {
        assertThat(user.getRole(), is(equalTo(oldRole)));

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        adminService.changeUserRole(userRoleDTO);

    }

    @Test
    public void change_UserRole_Success() {
        assertThat(user.getRole(), is(equalTo(oldRole)));

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(roleService.getRoleByName(anyString())).thenReturn(testEngineerRole);
        adminService.changeUserRole(userEngineerRoleDTO);

        assertThat(user.getRole(), is(equalTo(testEngineerRole)));
    }

    @Test(expected = BadRoleException.class)
    public void change_UserRole_Exception() {
        assertThat(user.getRole(), is(equalTo(oldRole)));

        when(userService.getUserByEmail(anyString())).thenReturn(user);
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
        user.setBlockedByAdmin(false);
        return user;
    }

    private UserRoleUpdateDTO getUserDtoFor(String userEmail, String userRole) {
        UserRoleUpdateDTO userRoleDTO = new UserRoleUpdateDTO();
        userRoleDTO.setEmail(userEmail);
        userRoleDTO.setRole(userRole);
        return userRoleDTO;
    }

    @Test
    public void setBlockedStatusForUser_CorrectUser_Success() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userDTOsTransformer.toUserDTO(user)).thenCallRealMethod();

        assertFalse(user.isBlockedByAdmin());
        UserDTO userDTO = adminService.setBlockedStatusForUser(2L, true);
        assertTrue(userDTO.getBlockedByAdmin());
    }

    @Test(expected = NotFoundException.class)
    public void setBlockedStatusForUser_WrongUserId_Success() {
        when(userService.getUserById(anyLong())).thenThrow(NotFoundException.class);

        UserDTO userDTO = adminService.setBlockedStatusForUser(33L, true);
    }
}