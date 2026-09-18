package com.springmatter.relearnspringboot.configuration;


import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseMigrationRunner implements CommandLineRunner {

    private final Flyway userFlyway;
    private final Flyway productFlyway;

    @Override
    public void run(String... args) throws Exception {
        userFlyway.migrate();
        productFlyway.migrate();
    }
}
