---
title: "Bug: Updating a category doesnt work (PUT /categories/1) 405"
labels: bug, required
---

**Phase 1 — CategoriesController (required).**

Users report that the product page returns a **405 Not Allowed** error when updating an
existing category as an admin.

**Reproduce:** call `PUT /categories/1` with updated category data (no new id, just the fields
to change).

### Tasks
- [ ] Reproduce the failed-update behavior (manual debugging + Insomnia)
- [ ] Find and fix the cause so the url returns **200 OK** and the category is updated in the DB,
you can prove this by running `http://localhost:8080/categories/1` and seeing the changed values.
- [ ] Add a unit test that proves the fix

### Done when
- running `http://localhost:8080/categories/1` returns the category with the updated values.
- A unit test covers the fixed behavior
