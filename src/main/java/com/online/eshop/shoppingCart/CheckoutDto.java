package com.online.eshop.shoppingCart;

import java.math.BigDecimal;

public class CheckoutDto {
    
    private ShoppingCartDto cart;
    private BigDecimal total_cost;

    public CheckoutDto(ShoppingCartDto cart, BigDecimal total_cost) {
        this.cart = cart;
        this.total_cost = total_cost;
    }
    public ShoppingCartDto getCart() {
        return cart;
    }
    public void setCart(ShoppingCartDto cart) {
        this.cart = cart;
    }
    public BigDecimal getTotal_cost() {
        return total_cost;
    }
    public void setTotal_cost(BigDecimal total_cost) {
        this.total_cost = total_cost;
    }   
}
