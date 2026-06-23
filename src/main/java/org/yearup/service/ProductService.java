package org.yearup.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> search(Integer categoryId, Double minPrice, Double maxPrice, String subCategory, Boolean isFeatured) {
        List<Product> products = categoryId != null
                ? productRepository.findByCategoryId(categoryId)
                : productRepository.findAll();

        return products.stream()
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .filter(p -> subCategory == null || subCategory.equalsIgnoreCase(p.getSubCategory()))
                .filter(p -> isFeatured == null || p.isFeatured() )
                .toList();
    }

    public List<Product> listByCategoryId(int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Product getById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public Product create(Product product) {
        product.setProductId(0);
        return productRepository.save(product);
    }

    public Product update(int productId, Product product) {

        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setCategoryId(product.getCategoryId());
        existing.setDescription(product.getDescription());
        existing.setSubCategory(product.getSubCategory());
        existing.setFeatured(product.isFeatured());
        existing.setImageUrl(product.getImageUrl());
        existing.setStock(product.getStock());
        return productRepository.save(existing);
    }

    public void delete(int productId) {
        productRepository.deleteById(productId);
    }
}
