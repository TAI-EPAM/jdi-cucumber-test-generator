package com.epam.test_generator.services;

import com.epam.test_generator.controllers.admin.request.UserRoleUpdateDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.BadRoleException;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AdminService {

    public static final String ADMIN_NAME = "ADMIN";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * Changes user role to role specified in userRoleUpdateDTO. Searching user occurs by e-mail
     * address.
     */
    public void changeUserRole(@NotNull UserRoleUpdateDTO userRoleUpdateDTO) {

        User userByEmail = userService.getUserByEmail(userRoleUpdateDTO.getEmail());
        String roleForChange = userRoleUpdateDTO.getRole();

        if (userByEmail == null) {
            throw new UnauthorizedException(
                "User with email: " + userRoleUpdateDTO.getEmail() + " not found.");
        }
        if (userByEmail.getRole().getName().equals(ADMIN_NAME)) {
            throw new BadRoleException("Prohibited to change admin's role");
        }
        if (roleForChange.equals(ADMIN_NAME)) {
            throw new BadRoleException("Prohibited to set admin role");
        }

        Role newRole = getRole(roleForChange);

        userByEmail.setRole(newRole);
    }

    private Role getRole(String roleName) {

        Role aRole = roleService.getRoleByName(roleName);

        if (aRole == null) {
            throw new BadRoleException("Invalid name for Role");
        }
        return aRole;
    }
}
