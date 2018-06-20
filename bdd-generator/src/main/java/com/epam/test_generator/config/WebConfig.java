package com.epam.test_generator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class customizing the default Java-based configuration for Spring MVC.
 * @see WebMvcConfigurer
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.epam.test_generator")
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure a handler to delegate unhandled requests by forwarding to the Servlet container's
     * "default" servlet.
     *
     * @param configurer default configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * Add handlers to serve static resources such as
     * images, js, and, css files from specific locations
     * under web application root, the classpath, and others.
     * @param registry stores registrations of resource handlers
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/views/vue-static/**")
            .addResourceLocations("/WEB-INF/vue-static/");
        registry.addResourceHandler("/index.html")
            .addResourceLocations("/WEB-INF/index.html");
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
    }
}