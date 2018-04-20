package com.epam.test_generator.config;

import com.epam.test_generator.entities.Role;
import com.epam.test_generator.services.JiraService;
import com.epam.test_generator.services.RoleService;
import com.epam.test_generator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * An application event listener interested in {@link ContextRefreshedEvent} which is raised
 * when an ApplicationContext gets initialized or refreshed.
 */
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JiraService jiraService;

    /**
     * Handle an application event: adds roles from roles.properties
     * file to the database and creates admin role if it doesn't exist
     *
     * @param event
     */
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        List<Role> rolesFromProperties = roleService.getRolesFromProperties();

        for (Role role : rolesFromProperties) {
            if (roleService.getRoleByName(role.getName()) == null) {
                roleService.addRole(role);
            }
        }

        userService.createAdminIfDoesNotExist();
    }


}
