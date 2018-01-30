package com.epam.test_generator.config.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.LoginService;
import com.epam.test_generator.services.UserService;
import com.epam.test_generator.services.exceptions.TokenMalformedException;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In this class, we are using Spring’s default AuthenticationManager, but we inject it with our own
 * AuthenticationProvider that does the actual authentication process. To implement this, we extend
 * the AbstractUserDetailsAuthenticationProvider, which requires us only to return UserDetails based
 * on the authentication request, in our case, the JWT token wrapped in the JwtAuthenticationToken
 * class. If the token is not valid, we throw an exception. However, if it is valid and decryption
 * by JwtUtil is successful, we extract the user details (userID) from token.
 */

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
        throws AuthenticationException {

    }


    /**
     * Validate and parse JWT token, check token expiration. When we got {@link
     * UsernamePasswordAuthenticationToken} we should cast it to our {@link JwtAuthenticationToken}
     * to get the token. After token validation we checked then the user wich encoded in this token
     * is exist. If user is real we get all roles of user and creating {@link AuthenticatedUser}
     * wich will be returned
     */
    @Override
    protected UserDetails retrieveUser(String username,
        UsernamePasswordAuthenticationToken authentication)
        throws AuthenticationException {

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();
        DecodedJWT jwt;
        try {
            jwt = loginService.validate(token);
        } catch (Exception e) {
            throw new TokenMalformedException("JWT token is not valid");
        }
        Long id = jwt.getClaim("id").asLong();
        User user = userService.getUserById(id);
        if (user == null) {
            throw new UnauthorizedException("no such user");


        }

        List<Role> roles = Collections.singletonList(user.getRole());
        Collection<SimpleGrantedAuthority> authorityList = getSimpleGrantedAuthorities(roles);

        return new AuthenticatedUser(user.getId(), user.getEmail(), token,
            authorityList, user.isLocked());


    }

    private Collection<SimpleGrantedAuthority> getSimpleGrantedAuthorities(List<Role> roles) {
        return roles.stream()
            .map(s -> "ROLE_" + s.getName())
            .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


    /**
     * Returns true if this AuthenticationProvider supports the indicated Authentication object.
     *
     * Returning true does not guarantee an AuthenticationProvider will be able to authenticate the
     * presented instance of the Authentication class. It simply indicates it can support closer
     * evaluation of it. An AuthenticationProvider can still return null from the
     * authenticate(Authentication) method to indicate another AuthenticationProvider should be
     * tried.
     *
     * Selection of an AuthenticationProvider capable of performing authentication is conducted at
     * runtime the ProviderManager.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);

    }
}
