package com.donald.service.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Configuration
public class JdbcUtil {

    @Bean
    public void setupJdbc() {
        try {
            Class.forName("org.postgresql.Driver");   // register the Driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public Connection getConn() throws SQLException {
        String uname = "postgres";
        String password = "12345678";
        String url = "jdbc:postgresql://localhost:5432/tripapp";
        return DriverManager.getConnection(url, uname, password);
    }
}
