package org.yearup.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        // get all categories
        return categoryRepository.findAll();
    }

    public Category getByCategoryId(int categoryId) {
        // get category by id
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public Category create(Category category) {
        // create a new category
        return categoryRepository.save(category);
    }

    public Category update(int categoryId, Category category) {
        // update category and return the updated category
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        existing.setCategoryId(categoryId);
        existing.setDescription(category.getDescription());
        existing.setName(category.getName());
        return categoryRepository.save(existing);
    }

    public void delete(int categoryId) {
        // delete category
        categoryRepository.deleteById(categoryId);
    }
}
