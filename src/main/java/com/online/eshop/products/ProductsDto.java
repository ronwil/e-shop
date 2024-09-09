package com.online.eshop.products;

public class ProductsDto {
    private Integer product_id;
    private int quantity;

    public ProductsDto() {
    }
    public Integer getProduct_id() {
        return product_id;
    }
    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }   
}