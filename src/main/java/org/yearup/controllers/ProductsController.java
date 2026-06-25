package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.models.User;
import org.yearup.service.ProductService;
import org.yearup.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductsController
{
    private final ProductService productService;
    private UserService userService;

    public ProductsController(ProductService productService)
    {
        this.productService = productService;
    }

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Product> search(@RequestParam(name="cat", required = false) Integer categoryId,
                                @RequestParam(name="minPrice", required = false) Double minPrice,
                                @RequestParam(name="maxPrice", required = false) Double maxPrice,
                                @RequestParam(name="subCategory", required = false) String subCategory,
                                @RequestParam(name="isFeatured",required = false) Boolean isFeatured)
    {
        return productService.search(categoryId, minPrice, maxPrice, subCategory,isFeatured);
    }

    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Product getById(@PathVariable int id)
    {
        Product product = productService.getById(id);

        if (product == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return product;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Product> addProduct(@RequestBody Product product)
    {
        Product saved = productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestBody Product product)
    {
        try {

            if (productService.getById(id) == null) {

                return ResponseEntity.status(404).body("Product not found for ID: " + id);
            }

            Product updated = productService.update(id, product);
            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Could not update product");
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id)
    {
        if (productService.getById(id) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
