package com.study.main;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 此处是JDBC与数据库连通性测试，不借助仍和数据库连接池
 */
@SpringBootTest(classes = {MainApplication.class})
public class JDBCTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
        String sql = "select count(*) from zelda_user";
        Long totle = jdbcTemplate.queryForObject(sql, Long.class);
        System.out.println(totle);
    }

}
