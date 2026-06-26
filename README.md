# EasyShop E-Commerce API — Capstone 3

A RESTful e-commerce backend built with Spring Boot 4, Java 17, MySQL, and JWT authentication. The API powers a full-featured online store with product browsing, category management, shopping cart, user profiles, and checkout.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| Security | Spring Security 7 + JWT (HS512) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL 8 |
| Build | Maven |
| API Docs | OpenAPI 3 / Swagger UI |
| Testing | JUnit 5, Mockito, Spring MVC Test |

---

## Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8 running locally

---

## Database Setup

1. Choose a store theme — SQL scripts are in `/database/`:
   - `create_database_easyshop.sql` (default)
   - `create_database_clothingstore.sql`
   - `create_database_grocerystore.sql`
   - `create_database_recordshop.sql`
   - `create_database_videogamestore.sql`

2. Run your chosen script against MySQL:
   ```bash
   mysql -u root -p < database/create_database_easyshop.sql
   ```

3. The script creates the database, tables, and seeds initial data including a default admin account (`admin` / `password`).

---

## Configuration

The app reads three environment variables for the database connection. Set them before running:

```bash
export DB_NAME=easyshop
export DB_USERNAME=root
export DB_PASSWORD=your_password
```

All other settings (JWT secret, token expiry, Swagger path) are pre-configured in `src/main/resources/application.properties`.

---

## Running the API

```bash
mvn spring-boot:run
```

Server starts on `http://localhost:8080`.

Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

---

## Authentication

All protected endpoints require a Bearer token in the `Authorization` header.

**Register a new user:**
```
POST /register
{
  "username": "johndoe",
  "password": "p@ssw0rd",
  "confirmPassword": "p@ssw0rd",
  "role": "USER"
}
```

**Login:**
```
POST /login
{
  "username": "johndoe",
  "password": "p@ssw0rd"
}
```

Returns a JWT token. Pass it on subsequent requests:
```
Authorization: Bearer <token>
```

Tokens expire after **30 hours**.

**Default admin credentials:** `admin` / `password`

---

## API Endpoints

### Authentication
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/register` | None | Create a new user account |
| POST | `/login` | None | Login and receive a JWT token |

### Categories
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/categories` | None | Get all categories |
| GET | `/categories/{id}` | None | Get category by ID |
| GET | `/categories/{id}/products` | None | Get all products in a category |
| POST | `/categories` | Admin | Create a new category |
| PUT | `/categories/{id}` | Admin | Update a category |
| DELETE | `/categories/{id}` | Admin | Delete a category |

### Products
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/products` | None | Search/list products (supports `cat`, `minPrice`, `maxPrice`, `subCategory` filters) |
| GET | `/products/{id}` | None | Get product by ID |
| POST | `/products` | Admin | Add a new product |
| PUT | `/products/{id}` | Admin | Update a product |
| DELETE | `/products/{id}` | Admin | Delete a product |

### Shopping Cart
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/cart` | User | Get current user's cart |
| POST | `/cart/products/{id}` | User | Add product to cart (or increment quantity) |
| PUT | `/cart/products/{id}` | User | Update quantity of a cart item |
| DELETE | `/cart` | User | Clear all items from cart |

### Profile
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/profile` | User | Get current user's profile |
| PUT | `/profile` | User | Update current user's profile |

---

## Roles

| Role | Access |
|---|---|
| `ROLE_USER` | Browse products, manage own cart and profile |
| `ROLE_ADMIN` | All user access + create/update/delete products and categories |

---

## Running Tests

```bash
mvn test
```

The test suite uses an in-memory H2 database and Mockito mocks — no MySQL connection required.

---

## Project Structure

```
src/
├── main/java/org/yearup/
│   ├── controllers/        # REST controllers (Categories, Products, Cart, Profile)
│   ├── models/             # JPA entities and DTOs
│   ├── repository/         # Spring Data JPA repositories
│   ├── security/           # JWT filter, entry points, WebSecurityConfig
│   └── service/            # Business logic layer
├── main/resources/
│   ├── application.properties
│   └── openapi.yaml        # Full API specification
└── test/java/org/yearup/
    ├── controller/         # MockMvc controller tests
    ├── service/            # Unit tests for service layer
    └── repository/         # JPA repository tests
```

---

## Front End

The API is designed to work with the EasyShop storefront — a static HTML/CSS/JavaScript client.

**Repository:** [https://github.com/Tkmucci/capstone-client-grocerystore](https://github.com/Tkmucci/capstone-client-grocerystore)

### Setup

```bash
git clone https://github.com/Tkmucci/capstone-client-grocerystore.git
cd capstone-client-grocerystore
```

Open `index.html` directly in your browser, or serve it with any static file server. The API must be running at `http://localhost:8080` before opening the front end.

### Front End Features

- **Browse products** — filter by category, price range, and subcategory
- **Product detail** — view full product information
- **Shopping cart** — add, update quantity, and remove items; running total updates live
- **User account** — register, login, and manage your profile
- **Admin panel** — create, edit, and delete products and categories (admin login required)

### API Base URL

The front end connects to `http://localhost:8080` by default. To change this (e.g. for a deployed backend), update the base URL in the front end JavaScript configuration.

---

## Resolved Issues

### Phase 1 — CategoriesController

| Issue | Problem | Fix |
|---|---|---|
| #06 | `GET /categories` returned 404 — controller had no `@RestController`, `@RequestMapping`, or `@CrossOrigin` annotations | Implemented full `CategoriesController` with all required annotations and `getAllCategories()` |
| #07 | `GET /categories/{id}` returned 404 | Implemented `getByCategoryId()` with proper path variable binding |
| #08 | `GET /categories/{id}/products` returned 404 | Implemented `getProductsByCategoryId()` delegating to `ProductService.listByCategoryId()` |
| #09 | `POST /categories` returned 405 | Implemented `addCategory()` with `@PostMapping` and `@PreAuthorize("hasAuthority('ROLE_ADMIN')")`, returns 201 Created |
| #10 | `PUT /categories/{id}` returned 405 | Implemented `updateCategory()` with `@PutMapping("/{id}")` and admin authorization |
| #11 | `DELETE /categories/{id}` returned 405 | Implemented `deleteCategory()` with `@DeleteMapping("/{id}")` and admin authorization, returns 204 No Content |

### Phase 2 — Bug Fixes

| Issue | Problem | Fix |
|---|---|---|
| #02 | Product search returned incomplete results — non-featured products were always filtered out | `ProductService.search()` had `.filter(Product::isFeatured)` hardcoded. Changed to `p -> isFeatured == null \|\| p.isFeatured()` so the featured filter only applies when explicitly requested |
| #03 | Updating a product did not save stock changes | `ProductService.update()` was missing `existing.setStock(product.getStock())`. Added the missing field assignment so all fields persist correctly |

### Phase 3 — Shopping Cart

| Issue | Problem | Fix |
|---|---|---|
| #04 | Shopping cart was entirely non-functional — controller and service were empty stubs | Implemented `ShoppingCartService` (`getByUserId`, `addToCart`, `updateCartItem`, `clearCart`) and wired up all four `ShoppingCartController` endpoints (`GET /cart`, `POST /cart/products/{id}`, `PUT /cart/products/{id}`, `DELETE /cart`) |

---

