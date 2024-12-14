package com.nashel.ms_cart.CartControllerIntegrationTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class CartControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String cartId;


    @Test
    @Order(1)
    void positiveTC1_CreateCart() throws Exception {
        String cartJSONString = "{"
                + "\"cartItems\": ["
                + "    {"
                + "        \"productId\": 2,"
                + "        \"itemsAmount\": 2"
                + "    },"
                + "    {"
                + "        \"productId\": 9,"
                + "        \"itemsAmount\": 5"
                + "    }"
                + "]"
                + "}";

        MvcResult result = mockMvc.perform(post("/api/rest/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartJSONString))
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.cartItems[?(@.product.id == 2)].itemsAmount").value(2))
                .andExpect(jsonPath("$.cartItems[?(@.product.id == 9)].itemsAmount").value(5))
                .andReturn();

        // Store for the other tests
        cartId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

    }

    @Test
    void negativeTC1_CreateCart() throws Exception {
        String cartJSONString = "{"
                + "\"cartItems\": ["
                + "    {"
                + "        \"productId\": 2,"
                + "        \"itemsAmount\": 2"
                + "    },"
                + "    {"
                + "        \"productId\": 2,"
                + "        \"itemsAmount\": 5"
                + "    }"
                + "]"
                + "}";

        mockMvc.perform(post("/api/rest/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartJSONString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void negativeTC2_CreateCart() throws Exception {
        String cartJSONString = "{"
                + "\"cartItems\": ["
                + "    {"
                + "        \"productId\": 2,"
                + "        \"itemsAmount\": 2"
                + "    },"
                + "    {"
                + "        \"productId\": null,"
                + "        \"itemsAmount\": 5"
                + "    }"
                + "]"
                + "}";

        mockMvc.perform(post("/api/rest/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartJSONString))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(2)
    void positiveTC1_GetCart() throws Exception {

        mockMvc.perform(get("/api/rest/cart/{cartId}", cartId))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.cartItems[?(@.product.id == 2)].itemsAmount").value(2))
                .andExpect(jsonPath("$.cartItems[?(@.product.id == 9)].itemsAmount").value(5));
    }

    @Test
    void negativeTC1_GetCart() throws Exception {

        mockMvc.perform(get("/api/rest/cart/{cartId}", 2222222))
                .andExpect(status().isBadRequest());
    }

    @Test
    void negativeTC2_GetCart() throws Exception {
        mockMvc.perform(get("/api/rest/cart/{cartId}", 3))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void positiveTC1_UpdateItemsCart() throws Exception {
        String cartJSONString =
                "{\"cartItems\": [{" +
                "    \"productId\": 2," +
                "    \"itemsAmount\": 2" +
                "}, {" +
                "    \"productId\": 3," +
                "    \"itemsAmount\": 3" +
                "}, {" +
                "    \"productId\": 4," +
                "    \"itemsAmount\": 6" +
                "}, {" +
                "    \"productId\": 7," +
                "    \"itemsAmount\": 1" +
                "}]}";

        mockMvc.perform(put("/api/rest/cart/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartJSONString))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.cartItems[?(@.product.id == 2)].itemsAmount").value(4))
                .andExpect(jsonPath("$.cartItems[?(@.product.id == 3)].itemsAmount").value(3))
                .andExpect(jsonPath("$.cartItems[?(@.product.id == 4)].itemsAmount").value(6))
                .andExpect(jsonPath("$.cartItems[?(@.product.id == 7)].itemsAmount").value(1))
                .andExpect(jsonPath("$.cartItems[?(@.product.id == 9)].itemsAmount").value(5));
    }

    @Test
    void negativeTC1_UpdateItemsCart() throws Exception {
        String cartJSONString = "{"
                + "\"cartItems\": ["
                + "    {"
                + "        \"productId\": 2,"
                + "        \"itemsAmount\": 2"
                + "    },"
                + "    {"
                + "        \"productId\": 2,"
                + "        \"itemsAmount\": 999999999"
                + "    }"
                + "]"
                + "}";

        mockMvc.perform(put("/api/rest/cart/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartJSONString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void negativeTC2_UpdateItemsCart() throws Exception {
        String cartJSONString = "{"
                + "\"cartItems\": ["
                + "    {"
                + "        \"productId\": \"\","
                + "        \"itemsAmount\": 2"
                + "    },"
                + "    {"
                + "        \"productId\": 2,"
                + "        \"itemsAmount\": 999999999"
                + "    }"
                + "]"
                + "}";

        mockMvc.perform(put("/api/rest/cart/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartJSONString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void positiveTC1_DeleteCart() throws Exception {

        mockMvc.perform(delete("/api/rest/cart/{cartId}", cartId))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.cartItems").exists());
    }

    @Test
    void negativeTC1_DeleteCart() throws Exception {

        mockMvc.perform(delete("/api/rest/cart/{cartId}", 123))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void negativeTC2_DeleteCart() throws Exception {

        mockMvc.perform(delete("/api/rest/cart/{cartId}", cartId))
                .andExpect(status().isNotFound());
    }
}
