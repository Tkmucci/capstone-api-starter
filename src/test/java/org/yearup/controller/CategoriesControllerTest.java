package org.yearup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.yearup.controllers.CategoriesController;
import org.yearup.models.Category;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriesController.class)
@Import(CategoriesControllerTest.SecurityConfig.class)class CategoriesControllerTest
{

    @TestConfiguration
    @EnableMethodSecurity
    static class SecurityConfig { }

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private ProductService productService;

    @Test
    void getAllCategories_shouldReturn200AndList() throws Exception
    {
        when(categoryService.getAllCategories())
                .thenReturn(List.of(new Category(1, "Electronics", "Gadgets")));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void getById_shouldReturn200AndCategory() throws Exception
    {
        when(categoryService.getByCategoryId(1))
                .thenReturn(new Category(1, "Electronics", "Gadgets"));

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addCategory_asAdmin_shouldReturn201() throws Exception
    {
        Category toCreate = new Category(0, "Books", "Printed books");
        Category created = new Category(10, "Books", "Printed books");
        when(categoryService.create(any(Category.class))).thenReturn(created);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(10));
    }

    @Test
    @WithMockUser(roles = "USER")
    void addCategory_asNonAdmin_shouldReturn403() throws Exception
    {
        when(categoryService.create(any(Category.class)))
                .thenReturn(new Category(10, "Books", "Printed books"));

        try {
            mockMvc.perform(post("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new Category(99, "Books", "Printed books"))))
                    .andReturn();
            fail("Expected ServletException to be thrown");
        } catch (jakarta.servlet.ServletException ex) {
            assertTrue(ex.getCause() instanceof AuthorizationDeniedException,
                    "Expected AuthorizationDeniedException but got: " + ex.getCause());
        }

        verify(categoryService, never()).create(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_asAdmin_shouldReturn200() throws Exception
    {
        Category updated = new Category(1, "Updated", "New desc");
        when(categoryService.update(eq(1), any(Category.class))).thenReturn(updated);

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_asAdmin_shouldReturn204() throws Exception
    {
        when(categoryService.existByID(1)).thenReturn(true);
        doNothing().when(categoryService).delete(1);

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(1);
    }
}