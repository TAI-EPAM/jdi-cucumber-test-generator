package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.TokenDAO;
import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.dto.RegistrationUserDTO;
import com.epam.test_generator.dto.UserDTO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import com.epam.test_generator.transformers.UserTransformer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private List<User> users;
    private List<UserDTO> userDTOS;

    @Mock
    private RoleService roleService;

    @Mock
    private UserTransformer transformer;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserDAO userDAO;

    @Mock
    private User user;

    @Mock
    private UserDTO userDTO;

    @Mock
    private LoginUserDTO loginUserDTO;

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
    public void setUp() throws Exception {
        users = new ArrayList<>();
        userDTOS = new ArrayList<>();
    }

    @Test
    public void getUser_ById_Valid() throws Exception {
        when(userDAO.findById(anyLong())).thenReturn(user);
        User userById = sut.getUserById(1L);
        assertNotNull(userById);

    }

    @Test
    public void getUserById_NoSuchUser_Success() throws Exception {
        when(userDAO.findById(anyLong())).thenReturn(null);
        User userById = sut.getUserById(1L);
        assertNull(userById);
    }

    @Test
    public void getUser_ByEmail_Success() throws Exception {
        when(userDAO.findByEmail(anyString())).thenReturn(user);
        User userById = sut.getUserByEmail("iteaky");
        assertNotNull(userById);
    }

    @Test
    public void getUserByEmail_NoSuchUser_Success() throws Exception {
        when(userDAO.findByEmail(anyString())).thenReturn(null);
        User userById = sut.getUserByEmail("iteaky");
        assertNull(userById);
    }

    @Test
    public void getAll_Users_Success() throws Exception {
        users.add(user);
        userDTOS.add(userDTO);
        when(userDAO.findAll()).thenReturn(users);
        when(transformer.toDtoList(users)).thenReturn(userDTOS);
        final List<UserDTO> usersDTO = sut.getUsers();
        assertFalse(usersDTO.isEmpty());
    }

    @Test
    public void getAll_EmptyDataBase_Success() throws Exception {
        when(userDAO.findAll()).thenReturn(users);
        final List<UserDTO> users = sut.getUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    public void createUser_RegistrationUserDTO_Success() throws Exception {
        sut.createUser(registrationUserDTO);
        verify(userDAO).save(any(User.class));
    }

    @Test(expected = UnauthorizedException.class)
    public void createUser_ExistUser_Success() throws Exception {
        when(sut.getUserByEmail(anyString())).thenReturn(user);
        sut.createUser(registrationUserDTO);
    }

    @Test
    public void isSamePasswords_True_Valid() throws Exception {
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        assertTrue(sut.isSamePasswords(anyString(), anyString()));

    }

    @Test
    public void isSamePasswords_False_Valid() throws Exception {
        when(encoder.matches(anyString(), anyString())).thenReturn(false);
        assertFalse(sut.isSamePasswords(anyString(), anyString()));

    }

    @Test
    public void updateFailureAttempts_UnlockedUser_UpdatedAndNotLocked() {
        int expectedAttempts = 3;
        int actualAttempts;

        User user = new User();
        user.setLocked(false);
        user.setAttempts(0);

        when(userDAO.findById(anyLong())).thenReturn(user);

        sut.updateFailureAttempts(1L);
        sut.updateFailureAttempts(1L);
        actualAttempts = sut.updateFailureAttempts(1L);

        verify(userDAO, times(3)).save(any(User.class));

        assertEquals(expectedAttempts, actualAttempts);
        assertEquals(expectedAttempts, (long) user.getAttempts());
        assertFalse(user.isLocked());
    }

    @Test
    public void updateFailureAttempts_UnlockedUser_UpdatedAndLocked() {
        int expectedAttempts = 5;
        int actualAttempts;

        User user = new User();
        user.setLocked(false);
        user.setAttempts(4);

        when(userDAO.findById(anyLong())).thenReturn(user);

        actualAttempts = sut.updateFailureAttempts(1L);
        verify(userDAO, times(1)).save(any(User.class));
        assertEquals(expectedAttempts, actualAttempts);
        assertTrue(user.isLocked());
    }

    @Test
    public void invalidateAttempts_LockedUser_UpdatedAndUnlocked() {
        int expectedAttempts = 0;

        User user = new User();
        user.setLocked(true);
        user.setAttempts(4);

        when(userDAO.findById(anyLong())).thenReturn(user);

        sut.invalidateAttempts(1L);
        assertEquals(expectedAttempts, (long) user.getAttempts());
        assertFalse(user.isLocked());
    }

    @Test
    public void createAdmin_ValidInputDate_Ok() throws Exception {
        sut.createAdminIfDoesNotExist();
        verify(userDAO).save(any(User.class));

    }

    @Test
    public void createAdmin_AlreadyExistedUser_Nok() throws Exception {
        when(userDAO.findByRole(roleService.getRoleByName("ADMIN")))
            .thenReturn(Collections.singletonList(new User()));
        sut.createAdminIfDoesNotExist();
        verify(userDAO, times(0)).save(any(User.class));
    }

    @Test
    public void confirmUser_SimpleToken_Ok() throws Exception {
        when(passwordService.getTokenByName(anyString())).thenReturn(token);
        when(token.getUser()).thenReturn(user);
        sut.confirmUser(anyString());

        verify(tokenDAO).delete(token);
    }

    @Test(expected = UnauthorizedException.class)
    public void confirmUser_IncorrectToken_Exception() throws Exception {
        when(passwordService.getTokenByName(anyString())).thenReturn(token);
        when(token.getUser()).thenReturn(null);
        sut.confirmUser(anyString());

        verify(tokenDAO).delete(token);
    }
}