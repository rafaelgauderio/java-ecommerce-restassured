package com.rafaeldeluca.dscommerce.controllers.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// annotations para carregar o contexto das aplicacoes
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private String productName;

    private String adminBearerToken;

    @BeforeEach
    public void setUp () {

        productName = "Smart TV";

    }

    // product mock data
    // {"id":2,"name":"Smart TV","price":2190.0,"imgUrl":"https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg"}
    @Test
    public void findAllProductsShouldReturnPageWhenProductNameIsNotEmpty  () throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get("/products?name={productName}",productName)
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content[0].id").value(2L));
        resultActions.andExpect(jsonPath("$.content[0].name").value("Smart TV"));
        resultActions.andExpect(jsonPath("$.content[0].price").value(2190.0));
        resultActions.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg"));
    }

    @Test
    public void findAllProductsShoudlReturnPageWithAllProductNameIsNotInform () throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        // nesse caso vai ter que retornar o primeiro objeto product da lista paginada

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content[0].id").value(1L));
        resultActions.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
        resultActions.andExpect(jsonPath("$.content[0].price").value(90.5));
        resultActions.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
    }

    @Test
    public void insertProductShoudlReturnProductDTOCreatedWhenUserLoggedAsAdmin () throws Exception {

        String jsonProductBody = "";

        ResultActions resultActions = mockMvc
                .perform(post("/products")
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .content(jsonProductBody)
                        .accept(MediaType.APPLICATION_JSON));

    }
}
