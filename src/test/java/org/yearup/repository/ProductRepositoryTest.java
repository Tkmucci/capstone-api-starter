package org.yearup.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.yearup.models.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest
{
    @Autowired
    private ProductRepository productRepository;

    @Test
    void findByCategoryId_shouldReturnMatchingProducts()
    {
        productRepository.save(new Product(0, "Laptop", 800.0, 1, "d", "Computers", 10, true, "i.png"));
        productRepository.save(new Product(0, "Shirt", 25.0, 2, "d", "Apparel", 50, false, "i.png"));

        List<Product> inCat1 = productRepository.findByCategoryId(1);

        assertEquals(1, inCat1.size());
        assertEquals("Laptop", inCat1.get(0).getName());
    }

    @Test
    void save_shouldGenerateId()
    {
        Product saved = productRepository.save(
                new Product(0, "Test", 10.0, 1, "d", "sub", 5, false, "i.png"));

        assertTrue(saved.getProductId() > 0);
    }

    @Test
    void findByCategoryId_shouldReturnEmpty_whenNoMatch()
    {
        List<Product> result = productRepository.findByCategoryId(999);

        assertTrue(result.isEmpty());
    }
}