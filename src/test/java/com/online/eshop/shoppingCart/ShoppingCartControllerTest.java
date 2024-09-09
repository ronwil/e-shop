package com.online.eshop.shoppingCart;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.eshop.product.Product;
import com.online.eshop.product.ProductRepository;
import com.online.eshop.product.ProductService;
import com.online.eshop.products.ProductsDto;
import com.online.eshop.products.ProductsRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class ShoppingCartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private ProductRepository productRepository;

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
        shoppingCartRepository.deleteAll();
    }

    @Test
    public void testCreateCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/carts"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
               .andExpect(MockMvcResultMatchers.jsonPath("$.checkedOut").value(false))
               .andExpect(MockMvcResultMatchers.jsonPath("$.products").isEmpty());
    }

    @Test
    public void testUpdateCartWithProduct() throws Exception {
        Set<String> labels = new HashSet<>();
        labels.add("food");
        Product product = new Product(null,"Laptop", BigDecimal.valueOf(999.99), LocalDate.now(), labels);
        Product savedProduct = productService.save(product);

        // Create a cart
        ShoppingCart cart = shoppingCartService.createCart();

        ProductsDto productsDto = new ProductsDto();
        productsDto.setProduct_id(savedProduct.getProduct_id());
        productsDto.setQuantity(4);

        // Add the product to the cart
        mockMvc.perform(MockMvcRequestBuilders.put("/api/carts/{id}", cart.getId()).contentType(MediaType.APPLICATION_JSON)
                                                                   .content(objectMapper.writeValueAsString(List.of(productsDto))))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.cart_id").value(cart.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.products[0].product_id").value(savedProduct.getProduct_id()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.products[0].quantity").value(4))
               .andExpect(MockMvcResultMatchers.jsonPath("$.checkedOut").value(false));
    }

    @Test
    public void testUpdateCartForFrozenCheckout() throws Exception {
        Set<String> labels = new HashSet<>();
        labels.add("food");
        Product product = new Product(null,"Laptop", BigDecimal.valueOf(999.99), LocalDate.now(), labels);
        Product savedProduct = productService.save(product);

        // Create a cart
        ShoppingCart cart = shoppingCartService.createCart();
        cart.setCheckedOut(true);
        shoppingCartRepository.save(cart);

        ProductsDto productsDto = new ProductsDto();
        productsDto.setProduct_id(savedProduct.getProduct_id());
        productsDto.setQuantity(4);

        // Add the product to the cart
        mockMvc.perform(MockMvcRequestBuilders.put("/api/carts/{id}", cart.getId()).contentType(MediaType.APPLICATION_JSON)
                                                                   .content(objectMapper.writeValueAsString(List.of(productsDto))))
               .andExpect(MockMvcResultMatchers.status().is(304))
               .andExpect(MockMvcResultMatchers.jsonPath("$").value("Cart is frozen and cannot be updated"));
    }

    @Test
    public void testCheckoutCart() throws Exception {
        Set<String> labels = new HashSet<>();
        labels.add("food");
        Product product = new Product(null,"Laptop", BigDecimal.valueOf(10.05), LocalDate.now(), labels);
        Product savedProduct = productService.save(product);

        // Create a cart
        ShoppingCart cart = shoppingCartService.createCart();

        ProductsDto productsDto = new ProductsDto();
        productsDto.setProduct_id(savedProduct.getProduct_id());
        productsDto.setQuantity(4);

        // Add the product to the cart
        mockMvc.perform(MockMvcRequestBuilders.put("/api/carts/{id}", cart.getId()).contentType(MediaType.APPLICATION_JSON)
                                                                   .content(objectMapper.writeValueAsString(List.of(productsDto))))
               .andExpect(MockMvcResultMatchers.status().isOk());

        // Add the product to the cart
        mockMvc.perform(MockMvcRequestBuilders.post("/api/carts/{id}/checkout", cart.getId()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.cart.cart_id").value(cart.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.cart.checkedOut").value(true))
               .andExpect(MockMvcResultMatchers.jsonPath("$.total_cost").value(40.20));
    }

    
}
