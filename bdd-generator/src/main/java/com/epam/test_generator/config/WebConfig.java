package com.epam.test_generator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
@ComponentScan("com.epam.test_generator")
public class WebConfig {

    /**
     * Add filter to spring context. ForwardedHeaderFilter extracts values from
     * "<code>Forwarded</code>" and "<code>X-Forwarded-*</code>" headers when a client communicates
     * with the server through a proxy.
     *
     * @return ForwardedHeaderFilter instance
     */
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}