package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.UserDAO;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.dto.UserDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import com.epam.test_generator.transformers.UserTransformer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class UserService {

    private final static String DEFAULT_ROLE = "GUEST";

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserTransformer userTransformer;

    public User getUserById(Long id) {
        return userDAO.findById(id);

    }

    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);

    }

    public void createAdminIfDoesNotExist() {

        final List<User> admin = userDAO.findByRole(roleService.getRoleByName("ADMIN"));

        if (admin.isEmpty()) {

            final User user = new User(
                    "admin@mail.com",
                    encoder.encode("admin"),
                    roleService.getRoleByName("ADMIN"));

            userDAO.save(user);
        }
    }

    public List<UserDTO> getUsers() {
        return userTransformer.toDtoList(userDAO.findAll());
    }

    public void createUser(LoginUserDTO loginUserDTO) {
        if (this.getUserByEmail(loginUserDTO.getEmail()) != null) {
            throw new UnauthorizedException(
                    "user with email:" + loginUserDTO.getEmail() + " already exist!");
        } else {

            final User user = new User(
                    loginUserDTO.getEmail(),
                    encoder.encode(loginUserDTO.getPassword()),
                    roleService.getRoleByName(DEFAULT_ROLE));

            userDAO.save(user);
        }
    }

    public boolean isSamePasswords(String passwordOne, String passwordTwo) {
        return encoder.matches(passwordOne, passwordTwo);
    }


}