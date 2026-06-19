---
title: "Bug: testing a failed delete"
labels: bug, required
---

**Phase 1 — CategoriesController (required).**

Users report that the category page returns a **405 Not Allowed** error when deleting an
existing category as an admin.

**Reproduce:** call `DELETE /categories/1` with **NO body**.

### Tasks
- [ ] Reproduce the failed-delete behavior (manual debugging + Insomnia)
- [ ] Find and fix the cause so the url returns **204 No Content** and the categories is removed
  from the DB, you can prove this by running `http://localhost:8080/categories` and confirming the
  category is gone from the list.
- [ ] Add a unit test that proves the fix

### Done when
- running `http://localhost:8080/categories/1` returns **404 Not Found** (the category no longer exists).
- running `http://localhost:8080/categories` no longer lists the deleted category.
- A unit test covers the fixed behavior
