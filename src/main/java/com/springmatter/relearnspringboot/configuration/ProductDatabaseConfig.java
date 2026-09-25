package com.springmatter.relearnspringboot.configuration;


import com.springmatter.relearnspringboot.repository.CategoryRepository;
import com.springmatter.relearnspringboot.repository.ProductRepository;
import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration

@EnableJpaRepositories(
        basePackages = "com.springmatter.relearnspringboot.repository",
        includeFilters = @ComponentScan.Filter(
                type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
                classes = {ProductRepository.class, CategoryRepository.class}
        ),
        excludeFilters = @ComponentScan.Filter(
                type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
                classes = {com.springmatter.relearnspringboot.repository.UserRepository.class, com.springmatter.relearnspringboot.repository.PatientRepository.class}
        ),
        entityManagerFactoryRef = "productEntityManagerFactory",
        transactionManagerRef = "productTransactionManager"
)
public class ProductDatabaseConfig {


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.product")
    public DataSource productDataSource() {

        return DataSourceBuilder.create().build();
    }

    @Bean
    @DependsOn("productFlyway")
    public LocalContainerEntityManagerFactoryBean productEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("productDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.springmatter.relearnspringboot.entity.product")
                .persistenceUnit("product")
                .build();
    }

    @Bean
    public PlatformTransactionManager productTransactionManager(
            @Qualifier("productEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(initMethod = "migrate")
    public Flyway productFlyway(@Qualifier("productDataSource") DataSource productDataSource) {
        return Flyway.configure()
                .dataSource(productDataSource)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .locations("db/migration/product")
                .load();


    }

}
