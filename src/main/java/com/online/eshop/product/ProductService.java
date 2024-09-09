package com.online.eshop.product;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.eshop.products.ProductsService;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private ProductsService productsService;
    
    
    public ProductService() {
    }

    @Autowired
    public ProductService(ProductRepository productRepository, ProductsService productsService) {
        this.productRepository = productRepository;
        this.productsService = productsService;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * Delete the product if not present in the shopping cart and cart is checked out.
     * @param id
     */
    public Boolean deleteById(Integer id) {
        Optional<Product> product = findById(id);
        if (product.isPresent() && !isProductCheckedOut(product.get())) {
            deleteProductsFromCarts(product.get());
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private Boolean isProductCheckedOut(Product product) {
        return productsService.findAllByProduct(product).stream().anyMatch(products -> products.getCart().getCheckedOut());
    }

    private void deleteProductsFromCarts(Product product) {
        productsService.findAllByProduct(product)
              .forEach(products -> productsService.deleteByProductsId(products.getId()));
    }
}
