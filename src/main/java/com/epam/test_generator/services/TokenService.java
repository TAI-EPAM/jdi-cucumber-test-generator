package com.epam.test_generator.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.test_generator.dto.LoginUserDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@PropertySource("classpath:application.properties")
@Transactional(noRollbackFor=UnauthorizedException.class)
public class TokenService {


    @Resource
    private Environment environment;

    @Autowired
    private UserService userService;

    public DecodedJWT validate(String token)
        throws IOException {

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(environment.getProperty("jwt_secret")))
            .withIssuer("cucumber")
            .build();
        return verifier.verify(token);
    }


    public String getToken(LoginUserDTO loginUserDTO) {

        User user = userService.getUserByEmail(loginUserDTO.getEmail());
        if (user == null) {
            throw new UnauthorizedException(
                    "User with email: " + loginUserDTO.getEmail() + " not found.");
        }

        if (user.isLocked()) {
            throw new UnauthorizedException("User Account is locked!");
        }

        if (!(userService.isSamePasswords(loginUserDTO.getPassword(), user.getPassword()))) {
            int attempts = userService.updateFailureAttempts(user.getId());
            if (user.isLocked()) {
                throw new UnauthorizedException("Incorrect password entered " + attempts + " times. "
                        + "User account has been locked!");
            }
            throw new UnauthorizedException("Incorrect password.");
        } else {
            Builder builder = JWT.create()
                .withIssuer("cucumber")
                .withClaim("id", user.getId());
            try {
                userService.invalidateAttempts(user.getId());
                return builder.sign(Algorithm.HMAC256(environment.getProperty("jwt_secret")));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }


}