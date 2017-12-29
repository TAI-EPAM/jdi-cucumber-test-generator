package com.epam.test_generator.config;

import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Tag;
import org.javers.core.Javers;
import org.javers.hibernate.integration.HibernateUnproxyObjectAccessHook;
import org.javers.repository.sql.ConnectionProvider;
import org.javers.repository.sql.DialectName;
import org.javers.repository.sql.JaversSqlRepository;
import org.javers.repository.sql.SqlRepositoryBuilder;
import org.javers.spring.auditable.AuthorProvider;
import org.javers.spring.auditable.MockAuthorProvider;
import org.javers.spring.jpa.JpaHibernateConnectionProvider;
import org.javers.spring.jpa.TransactionalJaversBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class JaversConfig {

    /**
     * Config javers to connect to existing sql repository. Set Tag and Step classes as Value type
     * for getting theirs copies in Case history.
     */
    @Bean
    public Javers javers(PlatformTransactionManager txManager) {
        JaversSqlRepository sqlRepository = SqlRepositoryBuilder
            .sqlRepository()
            .withConnectionProvider(jpaConnectionProvider())
            .withDialect(DialectName.H2)
            .build();

        return TransactionalJaversBuilder
            .javers()
            .withTxManager(txManager)
            .withObjectAccessHook(new HibernateUnproxyObjectAccessHook())
            .registerJaversRepository(sqlRepository)
            .registerValue(Tag.class)
            .registerValue(Step.class)
            .build();
    }

    /**
     * Required by auto-audit aspect. <br/><br/>
     *
     * Creates {@link MockAuthorProvider} instance, suitable when using Spring Security
     */
    @Bean
    public AuthorProvider authorProvider() {
        return new MockAuthorProvider();
    }

    @Bean
    public ConnectionProvider jpaConnectionProvider() {
        return new JpaHibernateConnectionProvider();
    }
}
