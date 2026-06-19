---
title: "Bug: Creating a category doesnt work (POST /categories) 405"
labels: bug, required
---

**Phase 1 — CategoriesController (required).**

Users report that the category page returns a **405 Not Allowed** error when creating a new
a category as an admin.

**Reproduce:** call `POST /categories` with category data.

### Tasks
- [ ] Reproduce the missing-categories behavior (manual debugging + Insomnia)
- [ ] Find and fix the cause so the url returns **201 Created** and the new
category is shows in the BD, you can prove this by running 
`http://localhost:8080/categories` you shou ld see the new category at the bottom.
- [ ] Add a unit test that proves the fix

### Done when
- running http://localhost:8080/categories returns all categories including the new one.
- A unit test covers the fixed behavior
