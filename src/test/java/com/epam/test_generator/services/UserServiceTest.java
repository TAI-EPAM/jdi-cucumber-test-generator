package com.epam.test_generator.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyChar;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import com.epam.test_generator.transformers.UserTransformer;
import org.junit.rules.ExpectedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    List<User> users;

    @Mock
    UserTransformer transformer;

    @Mock
    PasswordEncoder encoder;
    @Mock
    UserDAO userDAO;

    @Mock
    User user;

    @Mock
    LoginUserDTO loginUserDTO;

    @InjectMocks
    UserService sut;

    @Before
    public void setUp() throws Exception {
        users = new ArrayList<>();


    }

    @Test
    public void getUserById() throws Exception {
        when(userDAO.findById(anyLong())).thenReturn(user);
        User userById = sut.getUserById(1L);
        assertNotNull(userById);

    }

    @Test
    public void getUserById_noSuchUser() throws Exception {
        when(userDAO.findById(anyLong())).thenReturn(null);
        User userById = sut.getUserById(1L);
        assertNull(userById);
    }

    @Test
    public void getUserByEmail() throws Exception {
        when(userDAO.findByEmail(anyString())).thenReturn(user);
        User userById = sut.getUserByEmail("iteaky");
        assertNotNull(userById);
    }

    @Test
    public void getUserByEmail_noSuchUser() throws Exception {
        when(userDAO.findByEmail(anyString())).thenReturn(null);
        User userById = sut.getUserByEmail("iteaky");
        assertNull(userById);
    }

    @Test
    public void getAll() throws Exception {
        users.add(user);
        when(userDAO.findAll()).thenReturn(users);
        List<User> users = sut.getAll();
        assertFalse(users.isEmpty());
    }

    @Test
    public void getAll_emptyDB() throws Exception {
        when(userDAO.findAll()).thenReturn(users);
        List<User> users = sut.getAll();
        assertTrue(users.isEmpty());
    }

    @Test
    public void createUser() throws Exception {
        sut.createUser(loginUserDTO);
        verify(userDAO).save(any(User.class));
    }

    @Test(expected = UnauthorizedException.class)
    public void createUser_ExistUser() throws Exception {
        when(sut.getUserByEmail(anyString())).thenReturn(user);
        sut.createUser(loginUserDTO);

    }

    @Test
    public void isSamePasswords_true() throws Exception {
        when(encoder.matches(anyString(),anyString())).thenReturn(true);
        assertTrue(sut.isSamePasswords(anyString(),anyString()));

    }

    @Test
    public void isSamePasswords_false() throws Exception {
        when(encoder.matches(anyString(),anyString())).thenReturn(false);
        assertFalse(sut.isSamePasswords(anyString(),anyString()));

    }

}