---
title: "Bug: Get products by Id does not show any items, returning a 404"
labels: bug, required
---

**Phase 1 — CategoriesController (required).**

Users report that the category page returns a **404 Not Found** error when searching for products in
a category by categoryId.

**Reproduce:** call `GET /categories/{id}/products` with no query parameters and compare the results against
the categories data in the database. Products under that category are not displayed from the DB but they exist.

### Tasks
- [ ] Reproduce the missing-categories behavior (manual debugging + Insomnia)
- [ ] Find and fix the cause so the url returns **every** matching products in the category
- [ ] Add a unit test that proves the fix

### Done when
- running http://localhost:8080/categories/1/products returns all category 1 products
- A unit test covers the fixed behavior
