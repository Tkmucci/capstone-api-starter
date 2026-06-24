package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);

        ShoppingCart cart = new ShoppingCart();
        for (CartItem item : cartItems) {

            ShoppingCartItem cartItem = new ShoppingCartItem();
            cartItem.setProduct(productService.getById(item.getProductId()));
            cartItem.setQuantity(item.getQuantity());
            cart.add(cartItem);
        }

        return cart;
    }

    // add additional methods here
    public ShoppingCart addToCart( int userId, int productId){

        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if(cartItem != null)
        {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            shoppingCartRepository.save(cartItem);
        }
        else
        {
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setProductId(productId);
            item.setQuantity(1);

            shoppingCartRepository.save(item);
        }

        return getByUserId(userId);
    }

    public ShoppingCart updateCartItem(int userId, int productId, int quantity){

        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        cartItem.setQuantity(quantity);
        shoppingCartRepository.save(cartItem);

        return getByUserId(userId);
    }

    public ShoppingCart clearCart(int userId) {

        List<CartItem> items = shoppingCartRepository.findByUserId(userId);
        shoppingCartRepository.deleteAll(items);

        return getByUserId(userId);
    }
}
