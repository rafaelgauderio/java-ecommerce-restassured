package com.rafaeldeluca.dscommerce.controllers.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldeluca.dscommerce.dto.OrderDTO;
import com.rafaeldeluca.dscommerce.dto.ProductDTO;
import com.rafaeldeluca.dscommerce.entities.*;
import com.rafaeldeluca.dscommerce.tests.ProductFactory;
import com.rafaeldeluca.dscommerce.tests.TokenUtil;
import com.rafaeldeluca.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingOrderId, nonExistingOrderId;
    private String productName;
    private Product product;
    private ProductDTO productDTO;
    private Order order;
    private OrderItem orderItem;
    private OrderDTO orderDTO;
    private User clientUser, adminUser;
    private String adminBearerToken, clientBearerToken, invalidBearerToken;
    private String adminUsername, clientUsername, adminPassword, clientPassword;

    @BeforeEach
    public void setUp() throws Exception {

        clientUsername = "carolina@gmail.com";
        clientPassword = "123456";
        adminUsername = "rafael@gmail.com";
        adminPassword = "123456";

       existingOrderId = 2L;
       nonExistingOrderId = 500L;


        adminBearerToken = tokenUtil.obtainsAccessToken(mockMvc, adminUsername, adminPassword);
        clientBearerToken = tokenUtil.obtainsAccessToken(mockMvc, clientUsername, clientPassword);
        invalidBearerToken = adminBearerToken + "concatenatedWithAnyString"; // simulating an invalid token

        clientUser = UserFactory.createClientUser();
        adminUser = UserFactory.createAdminUser();
        order = new Order(null, Instant.now(), OrderStatus.PAID, clientUser,new Payment());

        product = ProductFactory.createProduct();
        orderItem = new OrderItem(order,product,10, 50.99);
        order.getItems().add(orderItem);



    }

    @Test
    void findByIdShouldReturnOrderDTOWhenIdExistsAndUserLoggedAsAdmin () {

    }
}