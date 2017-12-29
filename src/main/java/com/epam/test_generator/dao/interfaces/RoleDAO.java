package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, Long> {

    com.epam.test_generator.entities.Role findByName(String name);

}
