package com.example.demo.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;


@SpringBootTest
class DbConfigTest {

    @Autowired
    DataSource dataSource;
    @Autowired
    TransactionManager transactionManager;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void checkBean() {
        Assertions.assertThat(dataSource).isNotNull();
        Assertions.assertThat(transactionManager).isNotNull();
        Assertions.assertThat(jdbcTemplate).isNotNull();
    }
}
