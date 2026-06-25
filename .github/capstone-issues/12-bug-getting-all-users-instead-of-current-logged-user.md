---
title: "Bug: Security issue: GET /profile returns all users instead of current logged user"
labels: bug, required
---

**Phase 4 — Security fix (required).**

⚠️: During phase 4, testing the user profile endpoint (`GET /profile`) revealed that it
returns all users instead of the current logged in user. It is a security issue and needs
urgent attention.

**Reproduce:** call `GET /profile` with no query parameters and compare the results against
the logged-in user in the database. it should return all the user's information only noone else's.

### Tasks
- [ ] Test and fix as you go and make sure it works by end of day (manual debugging + Insomnia)
- [ ] Add a unit test that proves the fix

### Done when
- Calling the endpoint returns the logged-in user's information only
- A unit test covers the fixed behavior
