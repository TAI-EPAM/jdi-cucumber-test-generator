package com.epam.test_generator.config;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Tag;
import org.javers.core.Javers;
import org.javers.hibernate.integration.HibernateUnproxyObjectAccessHook;
import org.javers.repository.sql.ConnectionProvider;
import org.javers.repository.sql.DialectName;
import org.javers.repository.sql.JaversSqlRepository;
import org.javers.repository.sql.SqlRepositoryBuilder;
import org.javers.spring.auditable.AuthorProvider;
import org.javers.spring.jpa.JpaHibernateConnectionProvider;
import org.javers.spring.jpa.TransactionalJaversBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Javers javersConfigForCase(PlatformTransactionManager txManager) {
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
     * Config javers to connect to existing sql repository. Set Tag and Case classes as Value type
     * for getting theirs copies in Suit history.
     */
    @Bean
    public Javers javersConfigForSuit(PlatformTransactionManager txManager) {
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
            .registerValue(Case.class)
            .build();
    }

    /**
     * Set up custom {@link AuthorProvider} to provide user email in commits info.
     */
    @Bean
    public AuthorProvider authorProvider() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null) {
                return "unauthenticated";
            }

            return ((AuthenticatedUser) auth.getPrincipal()).getEmail();
        };
    }

    @Bean
    public ConnectionProvider jpaConnectionProvider() {
        return new JpaHibernateConnectionProvider();
    }
}
