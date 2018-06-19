package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.RoleDAO;
import com.epam.test_generator.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class RoleService {

    @Resource
    private Environment environment;

    @Autowired
    private RoleDAO roleDAO;

    public Role getRoleByName(String name) {
        return roleDAO.findByName(name);
    }

    /**
     * Adds role to database via roleDAO
     * @param role role to add
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRole(Role role) {
        roleDAO.save(role);
    }

    /**
     * Returns all roles from database in list
     * @return list of all roles from db
     */
    public List<Role> findAll() {
        return roleDAO.findAll();
    }
}
