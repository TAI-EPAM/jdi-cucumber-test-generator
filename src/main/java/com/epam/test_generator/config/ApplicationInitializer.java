package com.epam.test_generator.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Class which configures the ServletContext in Servlet 3.0+ environments programmatically -
 * as opposed to (or possibly in conjunction with) the traditional web.xml-based approach.
 * This class is automatically detected by SpringServletContainerInitializer,
 * which itself is bootstrapped automatically by any Servlet 3.0 container.
 * See its <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/SpringServletContainerInitializer.html">
 * Javadoc</a> for details on this bootstrapping mechanism.
 */
public class ApplicationInitializer implements WebApplicationInitializer {

    private final static String DISPATCHER = "dispatcher";

    /**
     * Configure the given {@link ServletContext}
     * with any servlets, filters, listeners context-params and attributes
     * necessary for initializing this web application.
     * @param servletContext the ServletContext to initialize
     * @throws ServletException if any call against the given ServletContext throws a ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();

        ctx.register(WebConfig.class);

        ServletRegistration.Dynamic servlet = servletContext
                .addServlet(DISPATCHER, new DispatcherServlet(ctx));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/*");
    }
}

