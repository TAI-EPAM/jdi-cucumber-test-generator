package com.epam.test_generator.config;

import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Properties;

/**
 * Configuration class customizing the default Java-based configuration for Spring MVC.
 * @see WebMvcConfigurerAdapter
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.epam.test_generator")
@PropertySource(value = "classpath:config.properties")
@PropertySource(value = "classpath:email.properties")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Resource
    private Environment environment;

    /**
     * Specialization of PlaceholderConfigurerSupport that resolves ${...} placeholders
     * within bean definition property values and @Value annotations against the
     * current Spring Environment and its set of PropertySources.
     * @return property sources placeholder configurer bean
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Set up custom {@link JavaMailSenderImpl} for mail functionality
     * @return Java mail sender bean
     */
    @Bean
    public JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(environment.getProperty("host"));
        mailSender.setPort(Integer.parseInt(environment.getProperty("port")));

        mailSender.setUsername(environment.getProperty("email.username"));
        mailSender.setPassword(environment.getProperty("password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    /**
     * Configure a handler to delegate unhandled requests by forwarding to the Servlet container's "default" servlet.
     * @param configurer default configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * Set up custom {@link ViewResolver} - an object that can resolve views by name.
     * @return view resolver bean
     */
    @Bean(name = "viewResolver")
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resourceViewResolver = new InternalResourceViewResolver();

        resourceViewResolver.setPrefix("/WEB-INF/static/views");
        resourceViewResolver.setSuffix(".html");

        return resourceViewResolver;
    }

    /**
     * Configure cross origin requests processing.
     * @param registry assists with the registration of CorsConfiguration mapped to a path pattern
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("*");
    }

    /**
     * Add handlers to serve static resources such as
     * images, js, and, css files from specific locations
     * under web application root, the classpath, and others.
     * @param registry stores registrations of resource handlers
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
            .addResourceLocations("/WEB-INF/static/");
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}