package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.TokenDAO;
import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.dto.RegistrationUserDTO;
import com.epam.test_generator.dto.UserDTO;
import com.epam.test_generator.entities.Token;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import com.epam.test_generator.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
public class UserService {

    public static final int MAX_ATTEMPTS = 5;

    private final static String DEFAULT_ROLE = "GUEST";

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserTransformer userTransformer;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private TokenDAO tokenDAO;

    public User getUserById(Long id) {
        return userDAO.findById(id);

    }

    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);

    }

    /**
     * Saves user to database
     * @param user
     */
    public void saveUser(User user) {
        userDAO.save(user);
    }


    /**
     * Creates in database user with admin role if none exists
     */
    public void createAdminIfDoesNotExist() {

        final List<User> admin = userDAO.findByRole(roleService.getRoleByName("ADMIN"));

        if (admin.isEmpty()) {

            final User user = new User(
                    "adminName",
                    "adminSurname",
                    "admin@mail.com",
                    encoder.encode("admin"),
                    roleService.getRoleByName("ADMIN"));

            userDAO.save(user);
        }
    }

    public List<UserDTO> getUsers() {
        return userTransformer.toDtoList(userDAO.findAll());
    }

    /**
     * Creates user with info specified in RegistrationUserDTO
     * @param registrationUserDTO user info
     * @return created user instance
     */
    public User createUser(RegistrationUserDTO registrationUserDTO) {
        if (this.getUserByEmail(registrationUserDTO.getEmail()) != null) {
            throw new UnauthorizedException(
                    "user with email:" + registrationUserDTO.getEmail() + " already exist!");
        } else {

            final User user = new User(
                    registrationUserDTO.getName(),
                    registrationUserDTO.getSurname(),
                    registrationUserDTO.getEmail(),
                    encoder.encode(registrationUserDTO.getPassword()),
                    roleService.getRoleByName(DEFAULT_ROLE));
            user.setLocked(true);
            userDAO.save(user);
            return user;
        }
    }

    public boolean isSamePasswords(String passwordOne, String passwordTwo) {
        return encoder.matches(passwordOne, passwordTwo);
    }

    /**
     * Method checks and increments the number of login attempts and locks the user in case of
     * exceeding the maximum number of attempts
     *
     * @param userId id of the identified user
     * @return number of incorrect attempts
     */
    public Integer updateFailureAttempts(Long userId) {
        User user = getUserById(userId);

        if (user != null) {
            int attempts = user.getAttempts();
            if (MAX_ATTEMPTS <= ++attempts) {
                user.setLocked(true);
            }
            user.setAttempts(attempts);
            userDAO.save(user);
            return attempts;
        }

        return 0;
    }

    /**
     * Cancels the count of incorrect login attempts for user and unlocked them
     *
     * @param userId id of the identified user
     */
    public void invalidateAttempts(Long userId) {
        User user = getUserById(userId);
        if (user != null) {
            user.setLocked(false);
            user.setAttempts(0);
            userDAO.save(user);
        }
    }

    /**
     * Updates user password by Email
     * @param password new password
     * @param email user's Email
     */
    public void updatePassword(String password, String email) {
        User byEmail = userDAO.findByEmail(email);
        byEmail.setPassword(password);
        byEmail.setLocked(false);
        byEmail.setAttempts(0);
        userDAO.save(byEmail);
    }

    public void checkUserExist(User user) {
        if (user == null) {
            throw new UnauthorizedException(
                    "User not found.");
        }
    }

    public void confirmUser(String token){
        tokenService.checkToken(token);
        Token tokenByName = passwordService.getTokenByName(token);
        User user = tokenByName.getUser();
        checkUserExist(user);
        user.setLocked(false);
        saveUser(user);
        tokenDAO.delete(tokenByName);
    }
}