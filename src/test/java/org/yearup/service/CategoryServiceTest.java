package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest
{
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category electronics;

    @BeforeEach
    void setup()
    {
        electronics = new Category(1, "Electronics", "Explore the latest gadgets.");
    }

    @Test
    void getById_shouldReturnCategory_whenItExists()
    {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(electronics));

        Category actual = categoryService.getByCategoryId(1);

        assertEquals(1, actual.getCategoryId());
        assertEquals("Electronics", actual.getName());
    }

    @Test
    void getById_shouldThrow_whenNotFound()
    {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> categoryService.getByCategoryId(99));
    }

    @Test
    void getAllCategories_shouldReturnAll()
    {
        when(categoryRepository.findAll()).thenReturn(List.of(electronics));

        List<Category> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void create_shouldSaveAndReturnCategory()
    {
        when(categoryRepository.save(electronics)).thenReturn(electronics);

        Category created = categoryService.create(electronics);

        assertEquals("Electronics", created.getName());
        verify(categoryRepository).save(electronics);
    }

    @Test
    void update_shouldModifyExistingCategory()
    {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(electronics));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        Category changes = new Category(1, "Updated Name", "Updated description");
        Category updated = categoryService.update(1, changes);

        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated description", updated.getDescription());
    }

    @Test
    void delete_shouldCallRepository_whenExists()
    {
        when(categoryRepository.existsById(1)).thenReturn(true);

        categoryService.delete(1);

        verify(categoryRepository).deleteById(1);
    }

    @Test
    void delete_shouldThrow_whenNotFound()
    {
        when(categoryRepository.existsById(99)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> categoryService.delete(99));
        verify(categoryRepository, never()).deleteById(anyInt());
    }
}