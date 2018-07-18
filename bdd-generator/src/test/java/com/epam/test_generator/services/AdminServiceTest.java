package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.controllers.admin.request.UserRoleUpdateDTO;
import com.epam.test_generator.controllers.user.UserDTOsTransformer;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.BadRoleException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {

    private static final String USER_NAME = "Name";
    private static final String USER_SURNAME = "Surname";
    private static final String USER_EMAIL = "test@test.com";
    private static final String USER_PASS = "qwerty";

    private static final String GUEST_ROLE_NAME = "GUEST";
    private static final String ADMIN_ROLE_NAME = "ADMIN";
    private static final String TEST_ENGINEER_ROLE_NAME = "TEST_ENGINEER";

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserDTOsTransformer userDTOsTransformer;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AdminService adminService;

    private User user;

    private Role guestRole;
    private Role adminRole;
    private Role testEngineerRole;


    @Before
    public void setUp() {
        guestRole = new Role(GUEST_ROLE_NAME);
        adminRole = new Role(ADMIN_ROLE_NAME);
        testEngineerRole = new Role(TEST_ENGINEER_ROLE_NAME);
        user = new User(USER_NAME, USER_SURNAME, USER_EMAIL, USER_PASS, adminRole);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
    }

    @Test
    public void changeUserRole_ToTestEngineerUserRole_Success() {
        user.setRole(guestRole);
        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(user);
        when(roleService.getRoleByName(TEST_ENGINEER_ROLE_NAME)).thenReturn(testEngineerRole);
        when(authenticatedUser.getEmail()).thenReturn(USER_EMAIL + "1");

        UserRoleUpdateDTO userRoleUpdateDTO = new UserRoleUpdateDTO();
        userRoleUpdateDTO.setEmail(USER_EMAIL);
        userRoleUpdateDTO.setRole(TEST_ENGINEER_ROLE_NAME);

        assertEquals(user.getRole(), guestRole);
        adminService.changeUserRole(userRoleUpdateDTO, authentication);
        assertEquals(user.getRole(), testEngineerRole);
    }

    @Test
    public void changeUserRole_ToAdminUserRole_Success() {
        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(user);
        when(roleService.getRoleByName(ADMIN_ROLE_NAME)).thenReturn(adminRole);
        when(authenticatedUser.getEmail()).thenReturn(USER_EMAIL + "1");

        UserRoleUpdateDTO userRoleUpdateDTO = new UserRoleUpdateDTO();
        userRoleUpdateDTO.setEmail(USER_EMAIL);
        userRoleUpdateDTO.setRole(ADMIN_ROLE_NAME);
        user.setRole(guestRole);

        assertEquals(user.getRole(), guestRole);
        adminService.changeUserRole(userRoleUpdateDTO, authentication);
        assertEquals(user.getRole(), adminRole);
    }

    @Test(expected = BadRoleException.class)
    public void changeUserRole_changeAdminRoleToGuest_BadRoleException() {
        when(authenticatedUser.getEmail()).thenReturn(USER_EMAIL);
        assertEquals(user.getRole(), adminRole);

        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(user);

        UserRoleUpdateDTO userRoleUpdateDTO = new UserRoleUpdateDTO();
        userRoleUpdateDTO.setEmail(USER_EMAIL);
        userRoleUpdateDTO.setRole(TEST_ENGINEER_ROLE_NAME);

        adminService.changeUserRole(userRoleUpdateDTO, authentication);
    }

    @Test(expected = UnauthorizedException.class)
    public void changeUserRole_InvalidUser_UnauthorizedException() {
        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(null);

        UserRoleUpdateDTO userRoleUpdateDTO = new UserRoleUpdateDTO();
        userRoleUpdateDTO.setEmail(USER_EMAIL);
        userRoleUpdateDTO.setRole(ADMIN_ROLE_NAME);

        adminService.changeUserRole(userRoleUpdateDTO, authentication);
    }

    @Test
    public void setBlockedStatusForUser_CorrectUser_Success() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userDTOsTransformer.toUserDTO(user)).thenCallRealMethod();

        user.setBlockedByAdmin(false);

        assertFalse(user.isBlockedByAdmin());
        UserDTO userDTO = adminService.setBlockedStatusForUser(2L, true);
        assertTrue(userDTO.getBlockedByAdmin());
    }

    @Test(expected = NotFoundException.class)
    public void setBlockedStatusForUser_WrongUserId_Success() {
        when(userService.getUserById(33L)).thenReturn(null);

        adminService.setBlockedStatusForUser(33L, true);
    }
}