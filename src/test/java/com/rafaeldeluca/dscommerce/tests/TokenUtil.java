package com.rafaeldeluca.dscommerce.tests;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Component
public class TokenUtil {

    @Value("${security.client-id}")
    private String clientId;

    @Value("${security.client-secret}")
    private String clientSecret;

    // method to get access token as a String
    public String obtainsAccessToken(MockMvc mockMvc, String username, String password) throws Exception {

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("grant_type", "password");
        parameters.add("username", username);
        parameters.add("password", password);

        ResultActions resultActions = mockMvc
                .perform(post("/oauth2/token")
                        .params(parameters)
                        .with(httpBasic(clientId, clientSecret))
                        .accept("application/json;charset=UTF-8"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
        return jacksonJsonParser.parseMap(resultString).get("access_token").toString();
    }
}
