package com.online.eshop.product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "product")
public class Product {
    public Product() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @NotEmpty(message = "Product name cannot be empty")
    @Column(unique = true)
    @Size(max = 200, message = "Product name cannot exceed 200 characters")
    private String name;
    private BigDecimal price;
    private LocalDate added_at;
    private Set<String> labels = new HashSet<>();

    public Product(Integer productId, String name,
            BigDecimal price, LocalDate added_at, Set<String> labels) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.added_at = added_at;
        this.labels = labels;
    }
    public Integer getProduct_id() {
        return productId;
    }
    public void setProduct_id(Integer productId) {
        this.productId = productId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public LocalDate getAdded_at() {
        return added_at;
    }
    public void setAdded_at(LocalDate added_at) {
        this.added_at = added_at;
    }
    public Set<String> getLabels() {
        return labels;
    }
    public void setLabels(HashSet<String> labels) {
        this.labels = labels;
    }
}
