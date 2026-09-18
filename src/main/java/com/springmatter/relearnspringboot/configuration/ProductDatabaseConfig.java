package com.springmatter.relearnspringboot.configuration;


import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ProductDatabaseConfig {


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.product")
    public DataSource productDataSource() {

        return DataSourceBuilder.create().build();
    }

    @Bean
    public Flyway productFlyway(@Qualifier("productDataSource") DataSource productDataSource) {
        return Flyway.configure()
                .dataSource(productDataSource)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .locations("db/migration/product")
                .load();


    }

}
