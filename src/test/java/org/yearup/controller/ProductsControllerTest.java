package org.yearup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.yearup.controllers.ProductsController;
import org.yearup.models.Product;
import org.yearup.service.ProductService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductsController.class)
class ProductsControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductService productService;

    @Test
    void search_shouldReturn200AndProducts() throws Exception
    {
        when(productService.search(any(), any(), any(), any()))
                .thenReturn(List.of(new Product(1, "Laptop", 800.0, 1, "d", "Computers", 10, true, "i.png")));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void getById_shouldReturn200_whenFound() throws Exception
    {
        when(productService.getById(1))
                .thenReturn(new Product(1, "Laptop", 800.0, 1, "d", "Computers", 10, true, "i.png"));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1));
    }

    @Test
    void getById_shouldReturn404_whenMissing() throws Exception
    {
        when(productService.getById(99)).thenReturn(null);

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addProduct_asAdmin_shouldReturn201() throws Exception
    {
        Product created = new Product(5, "New", 50.0, 1, "d", "sub", 5, false, "i.png");
        when(productService.create(any(Product.class))).thenReturn(created);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(5));
    }

    @Test
    @WithMockUser(roles = "USER")
    void addProduct_asNonAdmin_shouldReturn403() throws Exception
    {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new Product(0, "x", 1.0, 1, "d", "s", 1, false, "i.png"))))
                .andExpect(status().isForbidden());

        verify(productService, never()).create(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_asAdmin_shouldReturn200() throws Exception
    {
        Product existing = new Product(1, "Laptop", 800.0, 1, "d", "Computers", 10, true, "i.png");
        Product updated = new Product(1, "Updated", 900.0, 1, "d", "Computers", 10, true, "i.png");
        when(productService.getById(1)).thenReturn(existing);
        when(productService.update(eq(1), any(Product.class))).thenReturn(updated);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct_asAdmin_shouldReturn204() throws Exception
    {
        when(productService.getById(1))
                .thenReturn(new Product(1, "Laptop", 800.0, 1, "d", "Computers", 10, true, "i.png"));

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).delete(1);
    }
}