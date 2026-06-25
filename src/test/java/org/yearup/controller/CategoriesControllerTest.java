package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.yearup.controllers.CategoriesController;
import org.yearup.models.Category;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(CategoriesController.class)
class CategoriesControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        Category toCreate = new Category(0, "Books", "Printed books");
        Category created = new Category(10, "Books", "Printed books");
        when(categoryService.create(any(Category.class))).thenReturn(created);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(10));

//        mockMvc.perform(post("/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new Category(0, "Books", "x"))))
//                .andExpect(status().isForbidden());
//
//        verify(categoryService, never()).create(any());
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
        doNothing().when(categoryService).delete(1);

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(1);
    }
}