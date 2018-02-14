package com.epam.test_generator.config;

import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Set up custom {@link Docket} - a builder which is intended to be the
     * primary interface into the swagger-springmvc framework.
     * @return docket bean
     */
    @Bean
    public Docket customImplementation() {

        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(getApiInfo())
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.epam.test_generator"))
            .paths(PathSelectors.any())
            .build();

    }

    private ApiInfo getApiInfo() {
        return new ApiInfo("Cucumber test generator REST API",
            "REST Api Documentation",
            "1.0",
            "urn:tos",
            new Contact("", "", ""),
            "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",
            new ArrayList<>());
    }
}
