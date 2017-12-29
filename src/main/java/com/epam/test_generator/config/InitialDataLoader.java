package com.epam.test_generator.config;

import com.epam.test_generator.entities.Role;
import com.epam.test_generator.services.RoleService;
import com.epam.test_generator.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class InitialDataLoader implements
    ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (roleService.findAll().isEmpty()) {
            roleService.addAllRolesToDB();
        }
        else {
            List<Role> rolesFromProperties = roleService.getRolesFromProperties();
            for(Role role: rolesFromProperties ){
                if(roleService.getRoleByName(role.getName()) == null){
                    roleService.addRole(role);
                }
            }
        }
    }
}
