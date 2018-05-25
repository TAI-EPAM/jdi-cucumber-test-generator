package com.epam.test_generator.controllers.user;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.user.request.RegistrationUserDTO;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserDTOsTransformerTest {

    @Mock
    private PasswordEncoder encoder;

    private User user;

    private static final String EMAIL = "email";
    private static final String PASSWORD = "pass";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final Integer ATTEMPTS = 0;
    private static final String ROLE = "ROLE";
    private static final Boolean LOCKED = false;
    private static final Long ID = 42L;

    @InjectMocks
    private UserDTOsTransformer userDTOsTransformer;

    @Before
    public void setUp() throws Exception {
        when(encoder.encode(anyString())).thenReturn(PASSWORD);
        user = new User();
        user.setName(NAME);
        user.setSurname(SURNAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    public void createEntityFromDTO_RegistrationDTO_Success() {
        RegistrationUserDTO registrationUserDTO = new RegistrationUserDTO();
        registrationUserDTO.setName(NAME);
        registrationUserDTO.setSurname(SURNAME);
        registrationUserDTO.setEmail(EMAIL);
        registrationUserDTO.setPassword(PASSWORD);

        User resultUser = userDTOsTransformer.fromDTO(registrationUserDTO);
        Assert.assertEquals(user, resultUser);
    }

    @Test
    public void createUserDTOFromEntity_User_Success() {
        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setName(NAME);
        expectedUserDTO.setSurname(SURNAME);
        expectedUserDTO.setEmail(EMAIL);
        expectedUserDTO.setRole(ROLE);
        expectedUserDTO.setLocked(LOCKED);
        expectedUserDTO.setAttempts(ATTEMPTS);
        expectedUserDTO.setId(ID);

        user.setRole(new Role(ROLE));
        user.setLoginAttempts(ATTEMPTS);
        user.setLocked(LOCKED);
        user.setId(ID);

        UserDTO resultUserDTO = userDTOsTransformer.toUserDTO(user);
        Assert.assertEquals(expectedUserDTO, resultUserDTO);
    }

    @Test
    public void toListUserDto_Users_Success() {
        user.setLocked(false);
        user.setLoginAttempts(ATTEMPTS);
        user.setRole(new Role(ROLE));

        UserDTO userDTO = new UserDTO();
        userDTO.setName(NAME);
        userDTO.setSurname(SURNAME);
        userDTO.setEmail(EMAIL);
        userDTO.setLocked(false);
        userDTO.setAttempts(ATTEMPTS);
        userDTO.setRole(ROLE);

        List<User> users = Collections.singletonList(user);
        List<UserDTO> expectedUserDTOS = Collections.singletonList(userDTO);

        List<UserDTO> resultUserDTOs = userDTOsTransformer.toListUserDto(users);
        Assert.assertEquals(expectedUserDTOS, resultUserDTOs);
    }

}
