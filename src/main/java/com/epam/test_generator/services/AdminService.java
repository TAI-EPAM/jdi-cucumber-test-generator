package com.epam.test_generator.services;

import com.epam.test_generator.dto.ChangeUserRoleDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.BadRoleException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional
@Service
public class AdminService {


    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public void changeUserRole(ChangeUserRoleDTO changeUserRoleDTO) {

        final User userByEmail = userService.getUserByEmail(changeUserRoleDTO.getEmail());

        if (userByEmail == null) {
            throw new UnauthorizedException(
                    "User with email: " + changeUserRoleDTO.getEmail() + " not found.");
        }
        final Role aNewRole = getRole(changeUserRoleDTO);

        userByEmail.setRole(aNewRole);
    }

    private Role getRole(ChangeUserRoleDTO changeUserRoleDTO) {

        final Role aRole = roleService.getRoleByName(changeUserRoleDTO.getRole());

        if (aRole == null) {
            throw new BadRoleException("Invalid name for Role");
        }
        return aRole;
    }


}
