package com.cydeo.service.impl;

import com.cydeo.model.Cart;
import com.cydeo.model.CartItem;
import com.cydeo.model.Product;
import com.cydeo.service.CartService;
import com.cydeo.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    public static Cart CART = new Cart(BigDecimal.ZERO, new ArrayList<>());

    private final ProductService productService;

    public CartServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public Cart getCart() {
        return CART;
    }

    @Override
    public Cart addToCart(UUID productId, Integer quantity) {
        //todo find product based on productId
        //todo initialise cart item using the found product
        //todo calculate cart total amount
        //todo add to cart
        Product productToAdd = productService.findProductById(productId);

        if (productToAdd.getRemainingQuantity()<quantity) return CART ;

        BigDecimal totalAmountToAdd = productToAdd.getPrice().multiply(BigDecimal.valueOf(1)); //1 is quantity
        CART.setCartTotalAmount(CART.getCartTotalAmount().add( totalAmountToAdd));

        boolean alreadyInCart = CART.getCartItemList().stream()
                .anyMatch(cartItem -> cartItem.getProduct().getId().toString().equals(productToAdd.getId().toString()));

        if (alreadyInCart) {
            CartItem foundCartItem = CART.getCartItemList().stream()
                    .filter(cartItem -> cartItem.getProduct().getId().toString().equals(productToAdd.getId().toString()))
                    .findFirst().orElseThrow();

            foundCartItem.setQuantity(foundCartItem.getQuantity() + 1);  //1 should be parameter 'quantity'
            foundCartItem.setTotalAmount(foundCartItem.getTotalAmount().add(totalAmountToAdd));  // 1 is quantity
        } else {
            CART.getCartItemList().add(new CartItem(productToAdd, 1, totalAmountToAdd));  //1 should be parameter 'quantity'
        }

        productToAdd.setRemainingQuantity(productToAdd.getRemainingQuantity()-1); //1 is quantity

        return CART;
    }

    @Override
    public boolean deleteFromCart(UUID productId) {
        //todo delete product object from cart using stream
        CartItem cartItemToDelete = CART.getCartItemList().stream()
                        .filter(cartItem -> cartItem.getProduct().getId().toString().equals(productId.toString()))
                                .findFirst().orElseThrow();

        CART.setCartItemList(
                CART.getCartItemList().stream()
                        .filter(cartItem -> !cartItem.getProduct().getId().toString().equals(productId.toString()))
                        .collect(Collectors.toList())
        );

        CART.setCartTotalAmount(CART.getCartTotalAmount().subtract(cartItemToDelete.getTotalAmount()));

        cartItemToDelete.getProduct().setRemainingQuantity(cartItemToDelete.getProduct().getRemainingQuantity() + cartItemToDelete.getQuantity());

        return true;
    }


}
