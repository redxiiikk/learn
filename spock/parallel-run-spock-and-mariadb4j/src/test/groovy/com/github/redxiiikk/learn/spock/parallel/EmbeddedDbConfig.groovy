package com.github.redxiiikk.learn.spock.parallel


import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService
import groovy.util.logging.Slf4j
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

import javax.sql.DataSource

@Slf4j
@Configuration
@EnableAutoConfiguration
class EmbeddedDbConfig {
    @Bean
    @Primary
    DataSource datasource(MariaDB4jSpringService mariaDB4jSpringService, DataSourceProperties dataSourceProperties) {
        mariaDB4jSpringService.getDB().createDB(dataSourceProperties.name)

        def config = mariaDB4jSpringService.getConfiguration()


        def dataSource = DataSourceBuilder.create()
                .username(dataSourceProperties.username)
                .password(dataSourceProperties.password)
                .url(config.getURL(dataSourceProperties.name))
                .driverClassName(dataSourceProperties.driverClassName)
                .build()

        log.info("start-migrate")

        def flyway = new Flyway(new FluentConfiguration().dataSource(dataSource).locations("filesystem:src/main/resources/db/migration"))
        flyway.migrate()

        log.info("end-migrate")

        return dataSource
    }
}
