package com.epam.test_generator.controllers.user;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.controllers.user.request.LoginUserDTO;
import com.epam.test_generator.dto.TokenDTO;
import com.epam.test_generator.services.LoginService;
import io.swagger.annotations.ApiImplicitParam;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controls login process.
 */
@RestController
public class LoginController {


    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginUserDTO userDTO,
                                          HttpServletRequest request) {
        loginService.checkPassword(userDTO, request);
        String token = loginService.getLoginJWTToken(userDTO);

        return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEST_LEAD", "ROLE_TEST_ENGINEER", "ROLE_GUEST"})
    @ApiImplicitParam(name = "Authorization", value = "add here your token",
        paramType = "header", dataType = "string", required = true)
    @GetMapping("/refresh-token")
    public ResponseEntity refreshToken(Authentication authentication) {
        String refreshToken = loginService
            .refreshToken(((AuthenticatedUser)authentication.getPrincipal()).getToken());

        return new ResponseEntity<>(new TokenDTO(refreshToken), HttpStatus.OK);
    }
}
