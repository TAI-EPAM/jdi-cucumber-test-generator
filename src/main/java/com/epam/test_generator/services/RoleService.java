package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.RoleDAO;
import com.epam.test_generator.entities.Role;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@PropertySource("classpath:roles.properties")
@Transactional
@Service
public class RoleService {

    @Resource
    private Environment environment;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UserService userService;

    public Role getRoleByName(String name) {
        return roleDAO.findByName(name);
    }

    public void addRoles(Collection<Role> roles) {
        roleDAO.save(roles);
    }

    public void addRole(Role role) {
        roleDAO.save(role);
    }

    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    public List<Role> getRolesFromProperties() {
        List<Role> roles = new ArrayList<>();
        String stringOfRoles = environment.getProperty("roles");
        String[] split = stringOfRoles.split(", ");
        List<String> strings = Arrays.asList(split);
        for (String s : strings) {
            roles.add(new Role(s));
        }
        return roles;
    }

    public void addAllRolesToDB() {
        roleDAO.save(getRolesFromProperties());
    }


}
