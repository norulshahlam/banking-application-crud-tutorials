package com.shah.bankingapplicationcrud;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.GET_ALL_CUSTOMERS;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "mysql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationTests {

    @LocalServerPort
    private int port;
    private static RestTemplate restTemplate;
    private String baseUrl = "http://localhost:";

    @BeforeAll
    static void beforeAll() {
        restTemplate = new RestTemplate();
    }
    @BeforeEach
    void setUp() {
        baseUrl = baseUrl.concat(String.valueOf(port)).concat("/api/v1");
    }

    @RepeatedTest(2)
    @Order(1)
    @DisplayName("Retrieve all customers")
    void createBooking() {
        log.info("Retrieve all customers");
        ResponseEntity<Customer> forEntity = restTemplate.getForEntity(baseUrl.concat(GET_ALL_CUSTOMERS), Customer.class);

        log.info("forEntity: " + forEntity);
    }

}
