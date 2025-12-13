package com.boscostay.searchservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Disabled because CI does not have DB/RabbitMQ")
class SearchserviceApplicationTests {

    @Test
    void contextLoads() {
    }
}
