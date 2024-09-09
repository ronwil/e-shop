package com.online.eshop.shoppingCart;

import java.util.ArrayList;
import java.util.List;

import com.online.eshop.products.ProductsDto;

public class ShoppingCartDto {
    private Long cart_id;
    private List<ProductsDto> products = new ArrayList<>();
    private Boolean checkedOut;

    public ShoppingCartDto(Long cart_id, List<ProductsDto> products, Boolean checkedOut) {
        this.cart_id = cart_id;
        this.products = products;
        this.checkedOut = checkedOut;
    }
    public Long getCart_id() {
        return cart_id;
    }
    public void setCart_id(Long cart_id) {
        this.cart_id = cart_id;
    }
    public Boolean getCheckedOut() {
        return checkedOut;
    }
    public void setCheckedOut(Boolean checkedOut) {
        this.checkedOut = checkedOut;
    }
    public List<ProductsDto> getProducts() {
        return products;
    }
    public void setProducts(List<ProductsDto> products) {
        this.products = products;
    }
    
}
