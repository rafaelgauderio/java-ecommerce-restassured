package com.rafaeldeluca.dscommerce.controllers.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldeluca.dscommerce.dto.ProductDTO;
import com.rafaeldeluca.dscommerce.entities.Category;
import com.rafaeldeluca.dscommerce.entities.Product;
import com.rafaeldeluca.dscommerce.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private ObjectMapper objectMapper;
    private String productName;
    private Product product;
    private ProductDTO productDTO;
    private String adminBearerToken, clientBearerToken, invalidBearerToken;
    private String adminUsername, clientUsername, adminPassword, clientPassword;

    @BeforeEach
    public void setUp() throws Exception {

        productName = "Smart TV";

        product = new Product(null, "TV LG 50 polegadas", "descricao da tv lg 50 polegadas", 4500.90, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg");
        Category category = new Category(3L, "Electronic");
        Category category2 = new Category(5L, "Television");
        product.getCategories().add(category);
        product.getCategories().add(category2);
        productDTO = new ProductDTO(product);

        clientUsername = "carolina@gmail.com";
        clientPassword = "123456";
        adminUsername = "rafael@gmail.com";
        adminPassword = "123456";

        adminBearerToken = tokenUtil.obtainsAccessToken(mockMvc, adminUsername, adminPassword);
        clientBearerToken = tokenUtil.obtainsAccessToken(mockMvc, clientUsername, clientPassword);
        invalidBearerToken = adminBearerToken + "concatenatedWithAnyString"; // simulating an invalid token
    }

    // product mock data
    // {"id":2,"name":"Smart TV","price":2190.0,"imgUrl":"https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg"}
    @Test
    public void findAllProductsShouldReturnPageWhenProductNameIsNotEmpty() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get("/products?name={productName}", productName)
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content[0].id").value(2L));
        resultActions.andExpect(jsonPath("$.content[0].name").value("Smart TV"));
        resultActions.andExpect(jsonPath("$.content[0].price").value(2190.0));
        resultActions.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg"));
    }

    @Test
    public void findAllProductsShoudlReturnPageWithAllProductNameIsNotInform() throws Exception {
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
    public void insertProductShouldReturnProductDTOCreatedWhenUserLoggedAsAdmin() throws Exception {

        String jsonProductBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc
                .perform(post("/products")
                        .header("Authorization", "Bearer " + adminBearerToken)
                        .content(jsonProductBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()); // comando para debuggar  test

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").value(26L)); // já tem 25 produtos no seed inicial
        resultActions.andExpect(jsonPath("$.name").value("TV LG 50 polegadas"));
        resultActions.andExpect(jsonPath("$.price").value(4500.90));
        resultActions.andExpect(jsonPath("$.description").value("descricao da tv lg 50 polegadas"));
        resultActions.andExpect(jsonPath("$.imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg"));
        resultActions.andExpect(jsonPath("$.categories[0].id").value(3l));
        resultActions.andExpect(jsonPath("$.categories[1].id").value(5l));
    }

}
