package com.epam.test_generator.transformers;

import com.epam.test_generator.dto.UserDTO;
import com.epam.test_generator.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserTransformer extends AbstractDozerTransformer<User, UserDTO> {

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Class<UserDTO> getDTOClass() {
        return UserDTO.class;
    }

}

