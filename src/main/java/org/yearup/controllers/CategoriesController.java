package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;

// add the annotations to make this a REST controller
@RestController

// add the annotation to make this controller the endpoint for the following url
// http://localhost:8080/categories
@RequestMapping("/categories")

// add annotation to allow cross-site origin requests
@CrossOrigin(origins = "*")
public class CategoriesController
{
    private final CategoryService categoryService;
    private final ProductService productService;

    // create an Autowired constructor to inject the categoryService and productService
    @Autowired
    public CategoriesController(CategoryService categoryService, ProductService productService){

        this.productService = productService;
        this.categoryService = categoryService;

    }

    // add the appropriate annotation for a get action
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getAllCategories()
    {

        try {

            // find and return all categories
            List<Category> categories = categoryService.getAllCategories();

            return ResponseEntity.ok(categories);

        } catch (RuntimeException e) {

            return ResponseEntity.status(500).body("Could not find the Category");
        }
    }

    // add the appropriate annotation for a get action
    @GetMapping("/{categoryId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getByCategoryId(@PathVariable int categoryId)
    {
        try {
            // get the category by id
            Category category = categoryService.getByCategoryId(categoryId);

            return ResponseEntity.ok(category);

        } catch (RuntimeException e) {

            return ResponseEntity.status(404).body("Could not find the Category");
        }
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable int categoryId)
    {

        try {

            // get a list of product by categoryId
            List<Product> products = productService.listByCategoryId(categoryId);

            if (products.isEmpty()) {

                return ResponseEntity.status(404).body("No products found for category ID: " + categoryId);
            }
            return ResponseEntity.ok(products);
        }
        catch (RuntimeException e) {

            return ResponseEntity.status(500).body("Could not find the Products for category ID: " + categoryId );
        }
    }


    // add annotation to call this method for a POST action
    @PostMapping

    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addCategory(@RequestBody Category category)
    {

        try {
            // insert the category and return it with status 201 Created
            Category created = categoryService.create(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return  ResponseEntity.status(500).body("Could not create the Category");
        }
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    @PutMapping("/{id}")
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        try {

            // update the category by id and return the updated category (200 OK)
            categoryService.update(id, category);
            return ResponseEntity.ok(category);
        }
        catch (RuntimeException e) {

            return ResponseEntity.status(404).body("category with ID: " + id + " not found");
        }
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    @DeleteMapping("/{id}")

    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")

    public ResponseEntity<?> deleteCategory(@PathVariable int id)
    {
        try {

            // delete the category by id and return status 204 No Content
            categoryService.delete(id);

            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {

            return ResponseEntity.status(404).body("Could not delete the Category");
        }
    }
}
