package com.example.g1_orderfoodonline.utils;

import com.example.g1_orderfoodonline.models.CartItem;
import com.example.g1_orderfoodonline.models.Food;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addToCart(CartItem newItem) {
        // Check if the food is already in cart
        for (CartItem item : cartItems) {
            if (item.getFood().getId() == newItem.getFood().getId()) {
                // Update quantity
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }

        // If not found, add new item
        cartItems.add(newItem);
    }

    public void removeFromCart(CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public void updateCartItemQuantity(CartItem cartItem, int quantity) {
        if (quantity <= 0) {
            removeFromCart(cartItem);
        } else {
            cartItem.setQuantity(quantity);
        }
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        return total;
    }
}

