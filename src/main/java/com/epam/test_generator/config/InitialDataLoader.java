package com.epam.test_generator.config;

import com.epam.test_generator.entities.Role;
import com.epam.test_generator.services.RoleService;
import com.epam.test_generator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;


@Component
public class InitialDataLoader implements
    ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        final List<Role> rolesFromProperties = roleService.getRolesFromProperties();

        for (Role role : rolesFromProperties) {
            if (roleService.getRoleByName(role.getName()) == null) {
                roleService.addRole(role);
            }
        }
        userService.createAdminIfDoesNotExist();
    }

}