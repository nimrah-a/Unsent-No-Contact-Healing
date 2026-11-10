package com.unsent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:unsenttest;DB_CLOSE_DELAY=-1","server.port=0"})
class UnsentApplicationTests { @Test void contextLoads(){} }
