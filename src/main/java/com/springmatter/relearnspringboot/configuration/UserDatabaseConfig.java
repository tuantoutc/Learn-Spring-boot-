package com.springmatter.relearnspringboot.configuration;


import com.springmatter.relearnspringboot.repository.CategoryRepository;
import com.springmatter.relearnspringboot.repository.PatientRepository;
import com.springmatter.relearnspringboot.repository.ProductRepository;
import com.springmatter.relearnspringboot.repository.UsersRepository;
import com.springmatter.relearnspringboot.repository.auth.RefreshTokenRepository;
import com.springmatter.relearnspringboot.repository.auth.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.springmatter.relearnspringboot.repository",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {UsersRepository.class,
                        PatientRepository.class,
                        UserRepository.class,
                        RefreshTokenRepository.class
                }
        ),
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {ProductRepository.class, CategoryRepository.class}
        ),
        entityManagerFactoryRef = "userEntityManagerFactory",
        transactionManagerRef = "userTransactionManager"
)
public class UserDatabaseConfig {


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSource userDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(initMethod = "migrate")
    public Flyway userFlyway(@Qualifier("userDataSource") DataSource userDataSource) {
        return Flyway.configure()
                .dataSource(userDataSource)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .locations("db/migration/user")
                .load();


    }

    @Bean
    @Primary
    @DependsOn("userFlyway")
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("userDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.springmatter.relearnspringboot.entity.user", "com.springmatter.relearnspringboot.entity.auth")
                .persistenceUnit("user")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager userTransactionManager(
            @Qualifier("userEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
