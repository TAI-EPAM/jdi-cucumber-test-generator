package com.epam.test_generator.config.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Role;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.LoginService;
import com.epam.test_generator.services.ProjectService;
import com.epam.test_generator.services.UserService;
import com.epam.test_generator.services.exceptions.UnauthorizedException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Security filter that performs jwt authentication only if valid Authorization header is present.
 * If token is not provided, request execution is continued and user would be soon marked anonymous
 * by {@link org.springframework.security.web.authentication.AnonymousAuthenticationFilter}. If
 * token is present, filter tries to parse it and if it is invalid, error returned immediately no
 * matter if request was made to guarded resource or not. On success, {@link Authentication} object
 * is created and placed to SecurityContextHolder.
 */
public abstract class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    /**
     * This method try to get Token as String from header or from the parameters. In case of sending
     * token in Header you should set header name "Authorization" and concatenate your token with
     * "Bearer " string.
     */
    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        String token = Optional.ofNullable(request.getHeader("Authorization"))
            .filter(a -> a.startsWith("Bearer "))
            .map(a -> a.replaceAll("^Bearer ", ""))
            .orElseGet(() -> request.getParameter("token"));

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            //Validate and parse JWT token, check token expiration.
            DecodedJWT decodedJwt = loginService.decodeJwt(token);

            Long id = decodedJwt.getClaim("id").asLong();
            User user = userService.getUserById(id);
            if (user == null) {
                throw new UnauthorizedException("No such user with id= " + id);
            }

            List<Role> roles = Collections.singletonList(user.getRole());
            Collection<SimpleGrantedAuthority> authorityList = getSimpleGrantedAuthorities(
                roles);

            List<Long> projectIds = projectService.getProjectsByUserId(user.getId()).stream()
                .map(Project::getId)
                .collect(Collectors.toList());

            AuthenticatedUser credentials = new AuthenticatedUser(user.getId(), user.getEmail(),
                token,
                authorityList, projectIds,
                user.isLocked());

            if (!credentials.isAccountNonLocked()) {
                setResponseErrorMsg(response, "User Account is locked!");
                return;
            }

            Authentication jwtAuthentication =
                new UsernamePasswordAuthenticationToken(credentials, "",
                    credentials.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        } catch (JWTDecodeException | SignatureVerificationException e) {
            setResponseErrorMsg(response, "JWT token is not valid");
            return;
        } catch (TokenExpiredException e) {
            setResponseErrorMsg(response, "JWT token has expired");
            return;
        } catch (Exception e) {
            setResponseErrorMsg(response, "JWT authentication failed");
            return;
        }

        chain.doFilter(request, response);
    }

    private Collection<SimpleGrantedAuthority> getSimpleGrantedAuthorities(List<Role> roles) {
        return roles.stream()
            .map(s -> "ROLE_" + s.getName())
            .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    /**
     * Abstract method for handle exceptions in JwtAuthenticationFilter
     *
     * @param response current response object
     * @param msg error message for output
     */
    abstract void setResponseErrorMsg(HttpServletResponse response, String msg) throws IOException;
}