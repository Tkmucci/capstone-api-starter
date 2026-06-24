package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

// convert this class to a REST controller
@RestController
@RequestMapping("cart")
@CrossOrigin(origins = "*")
// only logged in users should have access to these actions
public class ShoppingCartController
{
    // a shopping cart controller depends on the service layer
    private ShoppingCartService shoppingCartService;
    private UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService){

        this.shoppingCartService = shoppingCartService;
        this.userService = userService;

    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    public ResponseEntity<ShoppingCart> getCart(Principal principal)
    {
        // get the currently logged in username
        String userName = principal.getName();
        // find database user by username
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        ShoppingCart cart = shoppingCartService.getByUserId(userId);

        // use the shoppingCartService to get all items in the cart and return the cart
        return ResponseEntity.ok(cart);
    }

    // add a POST method to add a product to the cart - the url should be
    @PostMapping("/products/{id}")

    // https://localhost:8080/cart/products/15  (15 is the productId to be added)

    public ResponseEntity<ShoppingCart> addToCart (@PathVariable int id, Principal principal){

        User user = userService.getByUserName(principal.getName());

        ShoppingCart cart = shoppingCartService.addToCart(user.getId(), id);

        // return the updated cart with status 201 Created
        return ResponseEntity.status(201).body(cart);
    }


    // add a PUT method to update an existing product in the cart - the url should be
    @PutMapping("/products/{id}")
    // https://localhost:8080/cart/products/15  (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)
    public ResponseEntity<ShoppingCart> updateCart(@PathVariable int id,
                                                       @RequestBody ShoppingCartItem item,
                                                       Principal principal)
    {

        User user = userService.getByUserName(principal.getName());

        ShoppingCart cartItem = shoppingCartService.updateCartItem(user.getId(), id, item.getQuantity());

        return ResponseEntity.ok(cartItem);

    }


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)

    @DeleteMapping
    public ResponseEntity<ShoppingCart> clearCart( Principal principal)
    {
        User user = userService.getByUserName(principal.getName());
        ShoppingCart cartItems = shoppingCartService.clearCart(user.getId());

        return ResponseEntity.ok(cartItems);
    }

}
