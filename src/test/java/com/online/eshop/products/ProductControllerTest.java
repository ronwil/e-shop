package com.online.eshop.products;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.online.eshop.product.Product;
import com.online.eshop.product.ProductRepository;
import com.online.eshop.product.ProductService;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductsRepository productsRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterAll
    public void deleteAll() {
        productsRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void testFindProduct() throws Exception {
        Set<String> labels = new HashSet<>();
        labels.add("food");
        Product product = new Product(null,"Laptop", BigDecimal.valueOf(999.99), LocalDate.now(), labels);
        Product savedProduct = productService.save(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}", savedProduct.getProduct_id()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Laptop"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("999.99"));
    }

    @Test
    public void testCreateProductWithNameExceedingMaxCharacters() throws Exception {
        Set<String> labels = new HashSet<>();
        labels.add("food");
        String longString = String.format("%0201d", 0);
        Product product = new Product(null,longString, BigDecimal.valueOf(999.99), null, labels);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products").contentType(MediaType.APPLICATION_JSON)
                                             .content(new ObjectMapper().writeValueAsString(product)))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation errors"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Product name cannot exceed 200 characters"));
    }

    @Test
    public void testCreateProductWithEmptyName() throws Exception {
        Set<String> labels = new HashSet<>();
        labels.add("food");
        Product product = new Product(null, "", BigDecimal.valueOf(999.99), null, labels);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products").contentType(MediaType.APPLICATION_JSON)
                                             .content(new ObjectMapper().writeValueAsString(product)))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation errors"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Product name cannot be empty"));
    }

    @Test
    public void testCreateProductWithSingleIncorrectLabel() throws Exception {
        Set<String> labels = new HashSet<>();
        labels.add("foodTest");
        Product product = new Product(null, "Label test", BigDecimal.valueOf(999.99), null, labels);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products").contentType(MediaType.APPLICATION_JSON)
                                             .content(new ObjectMapper().writeValueAsString(product)))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$").value("Incorrect label value"));
    }

    @Test
    public void testCreateProductWithIncorrectLabelString() throws Exception {
        Set<String> labels = new HashSet<>();
        labels.add("food");
        labels.add("limited");
        labels.add("test");
        Product product = new Product(null, "Label test", BigDecimal.valueOf(999.99), null, labels);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products").contentType(MediaType.APPLICATION_JSON)
                                             .content(new ObjectMapper().writeValueAsString(product)))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$").value("Incorrect label value"));
    }

    @Test
    public void testCreateProductNameAlreadyExists() throws Exception {
        Set<String> labels = new HashSet<>();
        labels.add("food");
        Product product = new Product(null,"Laptop", BigDecimal.valueOf(999.99), LocalDate.now(), labels);
        productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products").contentType(MediaType.APPLICATION_JSON)
                                             .content(objectMapper.writeValueAsString(product)))
               .andExpect(MockMvcResultMatchers.status().isConflict())
               .andExpect(MockMvcResultMatchers.jsonPath("$").value("Product name already exists"));
    }

    /**
     * Test functionality to delete cart relationship then delete the product itself.
     * @throws Exception
     */
    @Test
    public void testDeleteProduct() throws Exception {
        Set<String> labels = new HashSet<>();
        labels.add("food");
        Product product = new Product(null,"Laptop", BigDecimal.valueOf(999.99), LocalDate.now(), labels);
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/{id}", savedProduct.getProduct_id()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}