package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
