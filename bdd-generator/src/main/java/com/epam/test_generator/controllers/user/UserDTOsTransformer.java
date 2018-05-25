package com.epam.test_generator.controllers.user;

import com.epam.test_generator.controllers.user.request.RegistrationUserDTO;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.entities.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDTOsTransformer {

    @Autowired
    private PasswordEncoder encoder;

    public List<UserDTO> toListUserDto(List<User> users) {
        return users.stream().map(this::toUserDTO)
            .collect(Collectors.toList());
    }

    public User fromDTO(RegistrationUserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        return user;
    }


   public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setEmail(user.getEmail());
        userDTO.setAttempts(user.getLoginAttempts());
        userDTO.setLocked(user.isLocked());
        if (user.getRole() != null) {
            userDTO.setRole(user.getRole().getName());
        }
        return userDTO;
   }

}
