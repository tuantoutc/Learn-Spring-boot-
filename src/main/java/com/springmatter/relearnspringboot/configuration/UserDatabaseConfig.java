package com.springmatter.relearnspringboot.configuration;


import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class UserDatabaseConfig {


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSource userDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean
    public Flyway userFlyway(@Qualifier("userDataSource") DataSource userDataSource) {
        return Flyway.configure()
                .dataSource(userDataSource)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .locations("db/migration/user")
                .load();


    }

}
