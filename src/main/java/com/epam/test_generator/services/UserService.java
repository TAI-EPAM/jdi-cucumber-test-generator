package com.epam.test_generator.services;

import static com.epam.test_generator.entities.Role.USER;

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

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserTransformer transformer;


    public User getUserById(Long id) {
        return userDAO.findById(id);

    }

    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);

    }

    public List<User> getAll() {
        return userDAO.findAll();

    }

    public void createUser(LoginUserDTO loginUserDTO) {
        if (this.getUserByEmail(loginUserDTO.getEmail()) != null) {
            throw new UnauthorizedException(
                "user with email:" + loginUserDTO.getEmail() + " already exist!");
        } else {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(loginUserDTO.getEmail());
            userDTO.setPassword(encoder.encode(loginUserDTO.getPassword()));
            User user = transformer.fromDto(userDTO);
            userDAO.save(user);
        }
    }

    public boolean isSamePasswords(String passwordOne, String passwordTwo) {
        return encoder.matches(passwordOne, passwordTwo);
    }


}