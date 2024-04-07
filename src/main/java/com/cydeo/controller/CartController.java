package com.cydeo.controller;

import com.cydeo.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class CartController {


    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String getCart(Model model){

        model.addAttribute("cart", cartService.getCart());

        return "cart/show-cart";
    }

    @GetMapping("/addToCart")
    public String addToCart(@RequestParam UUID productId){

        cartService.addToCart(productId, 1);

        return "redirect:/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam UUID productId){

        cartService.deleteFromCart(productId);

        return "redirect:/cart";
    }

    /*@PostMapping("/cart")
    public String addCart(){


    }*/
}
