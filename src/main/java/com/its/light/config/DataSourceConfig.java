package com.its.light.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataSourceConfig {

    @Bean
    DataSource getDataSource() throws URISyntaxException {
        URI jdbUri = new URI(System.getenv("JAWSDB_URL"));
        String username = jdbUri.getUserInfo().split(":")[0];
        String password = jdbUri.getUserInfo().split(":")[1];
        String host = jdbUri.getHost();

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
        dataSourceBuilder.url( host );
        dataSourceBuilder.username( username );
        dataSourceBuilder.password( password );

        return dataSourceBuilder.build();
    }
}
