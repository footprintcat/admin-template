package com.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("develop")
@org.springframework.boot.test.context.SpringBootTest(classes = BackendApplication.class,
        webEnvironment = org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataJdbc
public class SpringBootTest {

    // @Resource
    // private XxxService xxxService;

    @Test
    public void testService() {
    }

}
