package com.online.eshop.shoppingCart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.eshop.products.ProductsDto;

@RestController
@RequestMapping("/api/carts")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<ShoppingCart> createCart() {
        return ResponseEntity.ok(shoppingCartService.createCart());
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCartDto>> getAllCarts() {
        return ResponseEntity.ok(shoppingCartService.getAllCarts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable Long id, @RequestBody List<ProductsDto> productsDtos) {
        ShoppingCartDto cart = shoppingCartService.updateCart(id, productsDtos);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        };
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Cart is frozen and cannot be updated");
    }

    @PostMapping("/{id}/checkout")
    public CheckoutDto checkoutCart(@PathVariable Long id) {
        return shoppingCartService.checkoutCart(id);
    }
}