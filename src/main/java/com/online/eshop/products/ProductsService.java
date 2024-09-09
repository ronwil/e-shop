package com.online.eshop.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.eshop.product.Product;

@Service
public class ProductsService {

    private ProductsRepository productsRepository;

    public ProductsService(){}

    @Autowired
    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<Products> findAllByProduct(Product product) {
        return productsRepository.findByProduct(product);
    }

    public void deleteByProductsId(Long id) {
        productsRepository.deleteById(id);
    }
    
}
