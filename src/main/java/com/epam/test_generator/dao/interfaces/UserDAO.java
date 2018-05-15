package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByRole(Role role);

}
