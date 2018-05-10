package com.epam.test_generator.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.test_generator.controllers.user.request.LoginUserDTO;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.TokenMalformedException;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@PropertySource("classpath:application.properties")
@Transactional(noRollbackFor = UnauthorizedException.class)
public class LoginService {


    private static final String ELEMENT_FOR_UNIQUE_TOKEN = "cucumber";
    private static final String JWT_SECRET_KEY = "jwt_secret";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_REFRESH_AFTER = "refresh_at";

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTTokenService jwtTokenService;

    @Resource
    private Environment environment;


    public DecodedJWT decodeJwt(String token)
            throws IOException {

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(environment.getProperty(JWT_SECRET_KEY)))
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
                    "Incorrect password entered %s times. User account has been locked!" +
                        " Mail for reset your password was send on your email.", attempts));
            }
            throw new UnauthorizedException(String.format("Incorrect password!" +
                    " You have %s attempts remaining before your account will be blocked!",
                UserService.MAX_ATTEMPTS - attempts));
        }
        userService.invalidateAttempts(user.getId());
    }

    public String getLoginJWTToken(LoginUserDTO userDTO) {
        User user = userService.getUserByEmail(userDTO.getEmail());
        return jwtTokenService.createJwtTokenFor(user, ELEMENT_FOR_UNIQUE_TOKEN, environment);
    }

    public String refreshToken(String token) {
        DecodedJWT jwt;
        try {
            jwt = decodeJwt(token);
        } catch (Exception e) {
            throw new TokenMalformedException("JWT token is not valid");
        }
        Date now = Date.from(Instant.now());
        String login = jwt.getClaim(CLAIM_EMAIL).asString();
        Date expiresAt = jwt.getExpiresAt();
        Date refreshAt = jwt.getClaim(CLAIM_REFRESH_AFTER).asDate();

        if (expiresAt.after(now) && refreshAt.before(now)) {
            token = jwtTokenService.createJwtTokenFor(userService.getUserByEmail(login),
                ELEMENT_FOR_UNIQUE_TOKEN, environment);
        }
        return token;
    }
}


