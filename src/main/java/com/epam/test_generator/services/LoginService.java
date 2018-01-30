package com.epam.test_generator.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
@PropertySource("classpath:application.properties")
@Transactional(noRollbackFor = UnauthorizedException.class)
public class LoginService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Resource
    private Environment environment;

    private final static String ELEMENT_FOR_UNIQUE_TOKEN = "cucumber";

    public DecodedJWT validate(String token)
            throws IOException {

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(environment.getProperty("jwt_secret")))
                .withIssuer(ELEMENT_FOR_UNIQUE_TOKEN)
                .build();
        return verifier.verify(token);
    }


    public void checkPassword(LoginUserDTO loginUserDTO, HttpServletRequest request) {
        User user = userService.getUserByEmail(loginUserDTO.getEmail());
        if (user == null) {
            throw new UnauthorizedException(
                    String.format("User with email: %s not found.", loginUserDTO.getEmail()));
        }

        if (user.isLocked()) {
            throw new UnauthorizedException("User Account is locked!");
        }

        if (!(userService.isSamePasswords(loginUserDTO.getPassword(), user.getPassword()))) {
            int attempts = userService.updateFailureAttempts(user.getId());
            if (user.isLocked()) {
                emailService.sendResetPasswordMessage(user, request);
                throw new UnauthorizedException(String.format(
                        "Incorrect password entered %s times. User account has been locked!"
                                + " Mail for reset your password was send on your email.", attempts));
            }
            throw new UnauthorizedException(String.format("Incorrect password!"
                            + " You have %s attempts remaining before your account will be blocked!",
                    UserService.MAX_ATTEMPTS - attempts));
        }
        userService.invalidateAttempts(user.getId());
    }

    public String getLoginJWTToken(LoginUserDTO loginUserDTO) {

        User user = userService.getUserByEmail(loginUserDTO.getEmail());
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(ELEMENT_FOR_UNIQUE_TOKEN)
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("given_name", user.getName())
                .withClaim("family_name", user.getSurname())
                .withClaim("role", user.getRole().getName());
        try {
            return builder.sign(Algorithm.HMAC256(this.environment.getProperty("jwt_secret")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}


