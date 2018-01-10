package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.dto.UserDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import com.epam.test_generator.transformers.UserTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private UserService sut;

    @Before
    public void setUp() throws Exception {
        users = new ArrayList<>();
        userDTOS = new ArrayList<>();
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
        userDTOS.add(userDTO);
        when(userDAO.findAll()).thenReturn(users);
        when(transformer.toDtoList(users)).thenReturn(userDTOS);
        final List<UserDTO> usersDTO = sut.getUsers();
        assertFalse(usersDTO.isEmpty());
    }

    @Test
    public void getAll_emptyDB() throws Exception {
        when(userDAO.findAll()).thenReturn(users);
        final List<UserDTO> users = sut.getUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    public void createUser() throws Exception {
      //  sut.createUser(loginUserDTO);
      //  verify(userDAO).save(any(User.class));
    }

    @Test(expected = UnauthorizedException.class)
    public void createUser_ExistUser() throws Exception {
        when(sut.getUserByEmail(anyString())).thenReturn(user);
        sut.createUser(loginUserDTO);

    }

    @Test
    public void isSamePasswords_true() throws Exception {
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        assertTrue(sut.isSamePasswords(anyString(), anyString()));

    }

    @Test
    public void isSamePasswords_false() throws Exception {
        when(encoder.matches(anyString(), anyString())).thenReturn(false);
        assertFalse(sut.isSamePasswords(anyString(), anyString()));

    }

}