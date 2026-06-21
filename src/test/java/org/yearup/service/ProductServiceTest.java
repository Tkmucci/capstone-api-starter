package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest
{
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product laptop;

    @BeforeEach
    void setup()
    {
        // featured = true so it survives the current search() filter
        laptop = new Product(1, "Laptop", 800.0, 1, "A laptop", "Computers", 10, true, "img.png");
    }

    @Test
    void getById_shouldReturnProduct_whenFound()
    {
        when(productRepository.findById(1)).thenReturn(Optional.of(laptop));

        Product result = productService.getById(1);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }

    @Test
    void getById_shouldReturnNull_whenNotFound()
    {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertNull(productService.getById(99));
    }

    @Test
    void listByCategoryId_shouldDelegateToRepository()
    {
        when(productRepository.findByCategoryId(1)).thenReturn(List.of(laptop));

        List<Product> result = productService.listByCategoryId(1);

        assertEquals(1, result.size());
        verify(productRepository).findByCategoryId(1);
    }

    @Test
    void create_shouldResetIdToZero_beforeSaving()
    {
        Product incoming = new Product(999, "New", 50.0, 1, "d", "sub", 5, true, "i.png");
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        productService.create(incoming);

        // verify the id was forced to 0 so DB generates it
        assertEquals(0, incoming.getProductId());
        verify(productRepository).save(incoming);
    }

    @Test
    void update_shouldModifyExistingFields()
    {
        when(productRepository.findById(1)).thenReturn(Optional.of(laptop));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product changes = new Product(0, "Updated", 900.0, 2, "new desc", "NewSub", 3, false, "new.png");
        Product result = productService.update(1, changes);

        assertEquals("Updated", result.getName());
        assertEquals(900.0, result.getPrice());
        assertEquals(2, result.getCategoryId());
    }

    @Test
    void update_shouldThrow_whenProductNotFound()
    {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class,
                () -> productService.update(99, laptop));
    }

    @Test
    void delete_shouldCallRepository()
    {
        productService.delete(1);

        verify(productRepository).deleteById(1);
    }

    @Test
    void search_shouldFilterByPriceRange()
    {
        Product cheap = new Product(2, "Mouse", 20.0, 1, "d", "Computers", 5, true, "m.png");
        when(productRepository.findByCategoryId(1)).thenReturn(List.of(laptop, cheap));

        List<Product> result = productService.search(1, 0.0, 100.0, null);

        // only the cheap featured item is in range
        assertEquals(1, result.size());
        assertEquals("Mouse", result.get(0).getName());
    }

    @Test
    void search_shouldOnlyReturnFeatured_currentBehavior()
    {
        // documents the current (likely buggy) featured-only filter
        Product notFeatured = new Product(3, "Cable", 5.0, 1, "d", "Computers", 5, false, "c.png");
        when(productRepository.findByCategoryId(1)).thenReturn(List.of(laptop, notFeatured));

        List<Product> result = productService.search(1, null, null, null);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isFeatured());
    }
}