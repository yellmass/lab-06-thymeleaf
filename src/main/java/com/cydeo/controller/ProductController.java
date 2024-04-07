package com.cydeo.controller;

import com.cydeo.model.CartItem;
import com.cydeo.model.Product;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.CartService;
import com.cydeo.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Data
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/list")
    public String getList(Model model){

        model.addAttribute("productList", productService.listProducts());
        model.addAttribute("totalQuantity", cartService.getCart().getCartItemList().stream()
                .map(CartItem::getQuantity)
                .reduce(0,(a,b)->a+b));

        return "product/list";
    }

    @PostMapping("/list")
    public String listInfo(@ModelAttribute Product product){

        productService.productCreate(product);

        return "redirect:/list";
    }

    @GetMapping("/create-form")
    public String productCreate(Model model){

        model.addAttribute("product", new Product());

        return "product/create-product";
    }

}
