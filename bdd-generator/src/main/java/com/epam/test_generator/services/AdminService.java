package com.epam.test_generator.services;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.controllers.admin.request.UserRoleUpdateDTO;
import com.epam.test_generator.controllers.user.UserDTOsTransformer;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.BadRoleException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AdminService {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserDTOsTransformer userDTOsTransformer;

    /**
     * Changes user role to role specified in userRoleUpdateDTO. Searching user occurs by e-mail
     * address.
     */
    public void changeUserRole(@NotNull UserRoleUpdateDTO userRoleUpdateDTO,
                               Authentication authentication) {
        User userByEmail = userService.getUserByEmail(userRoleUpdateDTO.getEmail());

        if (userByEmail == null) {
            throw new UnauthorizedException(
                "User with email: " + userRoleUpdateDTO.getEmail() + " not found.");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof AuthenticatedUser &&
            ((AuthenticatedUser) principal).getEmail() != null)) {
            throw new BadRoleException("Bad credentials");
        }

        String email = ((AuthenticatedUser) principal).getEmail();

        if (email.equals(userByEmail.getEmail())) {
            throw new BadRoleException("Admin cannot change role for himself");
        }

        Role newRole = getRole(userRoleUpdateDTO.getRole());
        userByEmail.setRole(newRole);
    }

    private Role getRole(String roleName) {

        Role aRole = roleService.getRoleByName(roleName);

        if (aRole == null) {
            throw new BadRoleException("Invalid name for Role");
        }
        return aRole;
    }


    public UserDTO setBlockedStatusForUser(long userId, boolean blocked) {

        User user = userService.getUserById(userId);

        if (user == null) {
            throw new NotFoundException("User with id: " + userId + " not found.");
        }

        user.setBlockedByAdmin(blocked);
        return userDTOsTransformer.toUserDTO(user);
    }
}
