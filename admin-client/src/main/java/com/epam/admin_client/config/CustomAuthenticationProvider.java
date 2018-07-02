package com.epam.admin_client.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.admin_client.service.LoginService;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final String ADMIN = "ADMIN";

    @Autowired
    private LoginService loginService;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        String token = loginService.authenticate(email, password);

        DecodedJWT decode = JWT.decode(token);
        Map<String, Claim> claims = decode.getClaims();

        String roleFromToken = claims.get("role").asString();

        if (ADMIN.equals(roleFromToken)) {
            AuthenticatedUser user = new AuthenticatedUser(email);
            Set<SimpleGrantedAuthority> roles = Collections
                .singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new UsernamePasswordAuthenticationToken(user, token, roles);
        }
        throw new BadCredentialsException("To access this client needs to have the ADMIN policies");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}