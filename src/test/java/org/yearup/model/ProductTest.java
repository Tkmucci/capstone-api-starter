package org.yearup.model;

import org.junit.jupiter.api.Test;
import org.yearup.models.Product;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest
{
    @Test
    void allArgsConstructor_shouldSetAllFields()
    {
        Product p = new Product(1, "Laptop", 800.0, 2, "desc", "Computers", 10, true, "img.png");

        assertEquals(1, p.getProductId());
        assertEquals("Laptop", p.getName());
        assertEquals(800.0, p.getPrice());
        assertEquals(2, p.getCategoryId());
        assertEquals("Computers", p.getSubCategory());
        assertEquals(10, p.getStock());
        assertTrue(p.isFeatured());
        assertEquals("img.png", p.getImageUrl());
    }

    @Test
    void setFeatured_shouldUpdateFlag()
    {
        Product p = new Product();
        p.setFeatured(true);
        assertTrue(p.isFeatured());
    }
}