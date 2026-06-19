---
title: "Bug: Get categories does not show any items, returning a 404"
labels: bug, required
---

**Phase 1 — CategoriesController (required).**

Users report that the category page returns a **404 Not Found** error.

**Reproduce:** call `GET /categories` with no query parameters and compare the results against
the categories data in the database. Some categories that exist are missing from the response.

### Tasks
- [ ] Reproduce the missing-categories behavior (manual debugging + Insomnia)
- [ ] Find and fix the cause so the url returns **every** matching category
- [ ] Add a unit test that proves the fix

### Done when
- running http://localhost:8080/categories returns all matching categories
- A unit test covers the fixed behavior
