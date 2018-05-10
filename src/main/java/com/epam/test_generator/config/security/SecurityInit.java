package com.epam.test_generator.config.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * By extending AbstractHttpSessionApplicationInitializer we ensure that the Spring Bean by the name
 * springSessionRepositoryFilter is registered with our Servlet Container for every request before
 * Spring Security’s springSecurityFilterChain.
 * NOTE: The existence of this class is not necessarily in Spring Boot configuration
 */
public class SecurityInit extends AbstractSecurityWebApplicationInitializer {

}