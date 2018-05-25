package com.epam.test_generator.config.security;

import com.epam.test_generator.dto.ErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.access.ExceptionTranslationFilter;

/**
 * WebSecurity configuration.
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment environment;

    /**
     * Disable defaults to exclude login/logout filters.
     *
     * @param environment Environment
     */
    public WebSecurityConfig(Environment environment) {
        super(true);
        this.environment = environment;
    }

    /**
     * {@link SessionCreationPolicy#STATELESS} instructs spring security not to try to persist session
     * since usage of cookies may lead to unexpected behaviour on iOS and Android. Anonymous
     * authentication is a fallback if client loads non-guarded resource
     *
     * @param http Security builder
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .anonymous().and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationErrorHandler())
            .accessDeniedHandler(accessDeniedHandler()).and()
            .headers().and()
            .securityContext().and()
            .servletApi().and()
            .cors().and()
            .addFilterAt(jwtAuthenticationFilter(), BasicAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers("/login", "/user/**", "/v2/api-docs",
                "/swagger-resources/**", "/configuration/**", "/swagger-ui.html",
                "/webjars/**", "/static/**", "/index.html")
            .permitAll()
            .antMatchers("/projects/{projectId}/**")
            .access("@webSecurityConfig.checkProjectId(authentication,#projectId)").and()
            .authorizeRequests()
            .anyRequest().authenticated();
    }

    /**
     * Common security filter for JWT token authorisation and authentication
     *
     * @return JwtAuthenticationFilter instance
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter() {
            @Override
            void setResponseErrorMsg(HttpServletResponse response, String msg) throws IOException {
                setErrorMessage(response, new ErrorDTO(msg));
            }
        };
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Method check that authorised user have access to some project
     *
     * @param authentication current authentication
     * @param projectId project id from path
     * @return true if principal has access to the project
     */
    public boolean checkProjectId(Authentication authentication, Long projectId) {
        AuthenticatedUser userDetails = (AuthenticatedUser) authentication.getPrincipal();
        return userDetails.getProjectIds().contains(projectId);
    }

    /**
     * Cors configuration
     *
     * @return Built cors configuration.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Custom AuthenticationEntryPoint handler for manage authentication exceptions
     * The AuthenticationEntryPoint only applies to non authenticated users when they try
     * to access to secure resources, when user user does not have authorization token
     * and request goes through {@link JwtAuthenticationFilter}
     * Instead redirect to login page we set error message in response.
     *
     * @return AuthenticationEntryPoint instance
     */
    @Bean
    public AuthenticationEntryPoint authenticationErrorHandler() {
        return (request, response, e) -> setErrorMessage(response, new ErrorDTO(e));
    }

    /**
     * Custom AccessDeniedHandler handler for manage authorisation exceptions
     * The AccessDeniedHandler only applies to authenticated users and used by
     * {@link ExceptionTranslationFilter} to handle an AccessDeniedException
     * Instead redirect to accessDeniedPage we set error message in response.
     *
     * @return AccessDeniedHandler instance
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, e) -> setErrorMessage(response, new ErrorDTO(e));
    }

    private void setErrorMessage(HttpServletResponse response, ErrorDTO errorDTO)
        throws IOException {

        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorDTO));
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}