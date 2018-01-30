package com.epam.test_generator.config.security;


import com.epam.test_generator.services.exceptions.TokenMissingException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public JwtAuthenticationFilter() {
        super("/**");
    }


    /**
     * This method try to get Token as String from header or from the parameters. In case of sending
     * token in Header you should set header name "Authorization" and concatenate your token with
     * "Bearer " string. If token was successfully received, method will call standard
     * Spring-Security Authenticated_Manager which will call {@link JwtAuthenticationProvider} which
     * will return {@link AuthenticatedUser} object.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        String token;
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            token = authorization.replaceFirst("^Bearer ", "");
        } else {
            token = request.getParameter("token");
        }
        if (token == null) {
            throw new TokenMissingException("No JWT token found in request headers");
        }

        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
        return getAuthenticationManager().authenticate(jwtAuthenticationToken);

    }

    /**
     * As this authentication is in HTTP header, after success we need to continue the request
     * normally and return the response as if the resource was not secured at all
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication authResult)
        throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        chain.doFilter(request, response);

    }
}


