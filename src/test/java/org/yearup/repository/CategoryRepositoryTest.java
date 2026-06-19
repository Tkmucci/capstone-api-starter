package org.yearup.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.yearup.models.Category;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest
{
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save_shouldPersistAndGenerateId()
    {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Created during test");

        Category saved = categoryRepository.save(category);

        assertTrue(saved.getCategoryId() > 0, "Saving should generate an id");
    }

    @Test
    void findById_shouldReturnSavedCategory()
    {
        Category category = new Category();
        category.setName("Electronics");
        category.setDescription("Gadgets");
        Category saved = categoryRepository.save(category);

        Optional<Category> found = categoryRepository.findById(saved.getCategoryId());

        assertTrue(found.isPresent());
        assertEquals("Electronics", found.get().getName());
        assertEquals("Gadgets", found.get().getDescription());
    }

    @Test
    void findAll_shouldReturnAllSavedCategories()
    {
        categoryRepository.save(new Category(0, "One", "First"));
        categoryRepository.save(new Category(0, "Two", "Second"));

        List<Category> all = categoryRepository.findAll();

        assertTrue(all.size() >= 2);
    }

    @Test
    void deleteById_shouldRemoveCategory()
    {
        Category saved = categoryRepository.save(new Category(0, "Temp", "To delete"));
        int id = saved.getCategoryId();

        categoryRepository.deleteById(id);

        assertFalse(categoryRepository.findById(id).isPresent());
    }
}