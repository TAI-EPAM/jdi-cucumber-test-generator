package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.user.UserDTOsTransformer;
import com.epam.test_generator.controllers.user.request.RegistrationUserDTO;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.dao.interfaces.TokenDAO;
import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String TEST_EMAIL_MAIL_COM = "testEmail@mail.com";
    private static final long USER_ID = 1L;
    private static final String USER_EMAIL = "iteaky";
    private static final String ROLE_ADMIN = "ADMIN";
    private List<User> users;
    private List<UserDTO> userDTOS;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserDAO userDAO;

    @Mock
    private User user;

    @Mock
    private UserDTOsTransformer userDTOsTransformer;

    @Mock
    private UserDTO userDTO;

    @Mock
    private RegistrationUserDTO registrationUserDTO;

    @Mock
    private EmailService emailService;

    @Mock
    private TokenService tokenService;

    @Mock
    private Token token;

    @Mock
    private PasswordService passwordService;

    @Mock
    private TokenDAO tokenDAO;

    @InjectMocks
    private UserService sut;

    @Before
    public void setUp() {
        users = new ArrayList<>();
        userDTOS = new ArrayList<>();
        registrationUserDTO = new RegistrationUserDTO();
        registrationUserDTO.setEmail(TEST_EMAIL_MAIL_COM);
    }

    @Test
    public void getUser_ById_Valid() {
        when(userDAO.findById(anyLong())).thenReturn(Optional.of(user));
        User userById = sut.getUserById(USER_ID);
        assertNotNull(userById);

    }

    @Test(expected = UnauthorizedException.class)
    public void getUserById_NoSuchUser_Success() {
        when(userDAO.findById(anyLong())).thenReturn(Optional.empty());
        sut.getUserById(USER_ID);
    }

    @Test
    public void getUser_ByEmail_Success() {
        when(userDAO.findByEmail(anyString())).thenReturn(user);
        User userById = sut.getUserByEmail(USER_EMAIL);
        assertNotNull(userById);
    }

    @Test
    public void getUserByEmail_NoSuchUser_Success() {
        when(userDAO.findByEmail(anyString())).thenReturn(null);
        sut.getUserByEmail(USER_EMAIL);
    }

    @Test
    public void getAll_Users_Success() {
        users.add(user);
        userDTOS.add(userDTO);
        when(userDAO.findAll()).thenReturn(users);
        when(userDTOsTransformer.toListUserDto(users)).thenReturn(userDTOS);
        List<UserDTO> usersDTO = sut.getUsers();
        assertFalse(usersDTO.isEmpty());
    }

    @Test
    public void getAll_EmptyDataBase_Success() {
        when(userDAO.findAll()).thenReturn(users);
        List<UserDTO> users = sut.getUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    public void createUser_RegistrationUserDTO_Success() {
        when(userDTOsTransformer.fromDTO(any())).thenReturn(user);
        sut.createUser(registrationUserDTO);
        verify(userDAO).save(any(User.class));
    }

    @Test(expected = UnauthorizedException.class)
    public void createUser_ExistUser_Success() {
        when(sut.getUserByEmail(registrationUserDTO.getEmail())).thenReturn(new User());
        sut.createUser(registrationUserDTO);
    }

    @Test
    public void isSamePasswords_True_Valid() {
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        assertTrue(sut.isSamePasswords(anyString(), anyString()));

    }

    @Test
    public void isSamePasswords_False_Valid() {
        when(encoder.matches(anyString(), anyString())).thenReturn(false);
        assertFalse(sut.isSamePasswords(anyString(), anyString()));

    }

    @Test
    public void updateFailureAttempts_UnlockedUser_UpdatedAndNotLocked() {
        int expectedAttempts = 3;
        int actualAttempts;

        User user = new User();
        user.unlock();
        user.setLoginAttempts(0);

        when(userDAO.findById(anyLong())).thenReturn(Optional.of(user));

        sut.updateFailureAttempts(USER_ID);
        sut.updateFailureAttempts(USER_ID);
        actualAttempts = sut.updateFailureAttempts(USER_ID);

        verify(userDAO, times(3)).save(any(User.class));

        assertEquals(expectedAttempts, actualAttempts);
        assertEquals(expectedAttempts, (long) user.getLoginAttempts());
        assertFalse(user.isLocked());
    }

    @Test
    public void updateFailureAttempts_UnlockedUser_UpdatedAndLocked() {
        int expectedAttempts = 5;
        int actualAttempts;

        User user = new User();
        user.setLocked(false);
        user.setLoginAttempts(4);

        when(userDAO.findById(anyLong())).thenReturn(Optional.of(user));

        actualAttempts = sut.updateFailureAttempts(USER_ID);
        verify(userDAO, times(1)).save(any(User.class));
        assertEquals(expectedAttempts, actualAttempts);
        assertTrue(user.isLocked());
    }

    @Test
    public void invalidateAttempts_LockedUser_UpdatedAndUnlocked() {
        int expectedAttempts = 0;

        User user = new User();
        user.setLocked(true);
        user.setLoginAttempts(4);

        when(userDAO.findById(anyLong())).thenReturn(Optional.of(user));

        sut.invalidateAttempts(USER_ID);
        assertEquals(expectedAttempts, (long) user.getLoginAttempts());
        assertFalse(user.isLocked());
    }

    @Test
    public void createAdmin_ValidInputDate_Ok() {
        sut.createAdminIfDoesNotExist();
        verify(userDAO).save(any(User.class));

    }

    @Test
    public void createAdmin_AlreadyExistedUser_Nok() {
        when(userDAO.findByRole(roleService.getRoleByName(ROLE_ADMIN)))
            .thenReturn(Collections.singletonList(new User()));
        sut.createAdminIfDoesNotExist();
        verify(userDAO, times(0)).save(any(User.class));
    }

    @Test
    public void confirmUser_SimpleToken_Ok() {
        when(tokenService.getTokenByName(anyString())).thenReturn(token);
        when(token.getUser()).thenReturn(user);
        sut.confirmUser(anyString());

        verify(tokenService).invalidateToken(token);
    }

    @Test(expected = UnauthorizedException.class)
    public void confirmUser_IncorrectToken_Exception() {
        when(tokenService.getTokenByName(anyString())).thenReturn(token);
        when(token.getUser()).thenReturn(null);
        sut.confirmUser(anyString());

        verify(tokenService).invalidateToken(token);
    }
}