package org.yearup.model;

import org.junit.jupiter.api.Test;
import org.yearup.models.Category;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest
{
    @Test
    void allArgsConstructor_shouldSetAllFields()
    {
        Category category = new Category(1, "Electronics", "Gadgets and devices");

        assertEquals(1, category.getCategoryId());
        assertEquals("Electronics", category.getName());
        assertEquals("Gadgets and devices", category.getDescription());
    }

    @Test
    void setters_shouldUpdateFields()
    {
        Category category = new Category();

        category.setCategoryId(5);
        category.setName("Books");
        category.setDescription("Printed and digital books");

        assertEquals(5, category.getCategoryId());
        assertEquals("Books", category.getName());
        assertEquals("Printed and digital books", category.getDescription());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyCategory()
    {
        Category category = new Category();

        assertEquals(0, category.getCategoryId());
        assertNull(category.getName());
        assertNull(category.getDescription());
    }
}