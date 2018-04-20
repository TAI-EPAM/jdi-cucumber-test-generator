package com.epam.test_generator.entities.api;

import com.epam.test_generator.entities.User;
import java.util.Set;

public interface UsersProjectTrait {

    Set<User> getUsers();

    String getName();


    default boolean hasUser(User user) {
        return getUsers().contains(user);
    }

    default boolean removeUser(User user) {
        return getUsers().remove(user);
    }

}
