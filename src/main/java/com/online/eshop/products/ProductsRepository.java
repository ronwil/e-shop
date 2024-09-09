package com.online.eshop.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.online.eshop.product.Product;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    
    List<Products> findByProduct(Product product);
}
