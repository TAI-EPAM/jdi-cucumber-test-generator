package com.epam.test_generator.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.annotation.Resource;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring configuration class for database connectivity
 */
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:db.properties")
@EnableJpaRepositories("com.epam.test_generator.dao.interfaces")
public class DatabaseConfig {

    @Resource
    private Environment environment;

    @Autowired
    private DataSource dataSource;

    /**
     * Set up custom {@link LocalContainerEntityManagerFactoryBean} to have shared JPA
     * in a Spring application context according to JPA's standard container bootstrap contract.
     * The JPA can then be passed to JPA-based DAOs via dependency injection.
     * @return entityManagerFactory bean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManager =
            new LocalContainerEntityManagerFactoryBean();

        entityManager.setDataSource(dataSource);
        entityManager.setPackagesToScan(environment.getRequiredProperty("db.entity.package"));
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManager.setJpaProperties(getHibernateProperties());

        return entityManager;
    }

    /**
     * Set up custom {@link DataSource} using H2 database.
     * @return dataSource
     */
    @Bean
    @Profile("!integration-tests")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:file:~/h2/app_db");

        return dataSource;
    }

    @Bean
    @Profile("integration-tests")
    public DataSource dataSourceForIntegrationTests() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testDB;DB_CLOSE_DELAY=-1");

        return dataSource;
    }

    /**
     * Sets up liquibase plugin for initializing database from changestets stored in resource folder
     * @return liquibase
     */
    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

    /**
     * Set up custom {@link JpaTransactionManager} and binds it to
     * entityManagerFactory bean to manage transactions for it
     * @return transactionManager binded to entityManagerFactory
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager manager = new JpaTransactionManager();

        manager.setEntityManagerFactory(entityManagerFactory().getObject());

        return manager;
    }

    /**
     * Load properties from hibernate.properties file to a {@link Properties} object.
     * @return properties object containing Hibernate properties if hibernate.properties file exists in classpath
     * @throws IllegalArgumentException if hibernate.properties file doesn't exist in classpath
     */
    private Properties getHibernateProperties() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("hibernate.properties");

            properties.load(inputStream);

            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "Cannot find 'hibernate.properties' file in classpath!");
        }
    }

}