package com.online.eshop.shoppingCart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.online.eshop.product.Product;
import com.online.eshop.product.ProductService;
import com.online.eshop.products.Products;
import com.online.eshop.products.ProductsDto;
import com.online.eshop.products.ProductsRepository;

@Service
public class ShoppingCartService {

    private ShoppingCartRepository shoppingCartRepository;
    private ProductsRepository productsRepository;
    private ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductsRepository productsRepository, ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productsRepository = productsRepository;
        this.productService = productService;
    }

    public ShoppingCart createCart() {
        ShoppingCart cart = new ShoppingCart();
        return shoppingCartRepository.save(cart);
    }

    public List<ShoppingCartDto> getAllCarts() {
        List<ShoppingCart> cartEntities = shoppingCartRepository.findAll();
        return convertToDto(cartEntities);
    }

    private List<ShoppingCartDto> convertToDto(List<ShoppingCart> cartEntities) {
        List<ShoppingCartDto> cartDtos = new ArrayList<>();
        for (ShoppingCart shoppingCartEntity : cartEntities) {
            List<ProductsDto> productsDtos = new ArrayList<>();
            for (Products products : shoppingCartEntity.getProducts()) {
                ProductsDto productsDto = new ProductsDto();
                productsDto.setProduct_id(products.getProduct().getProduct_id());
                productsDto.setQuantity(products.getQuantity());
                productsDtos.add(productsDto);
            }
            ShoppingCartDto cart = new ShoppingCartDto(shoppingCartEntity.getId(), productsDtos, shoppingCartEntity.getCheckedOut());
            cartDtos.add(cart);
        }
        return cartDtos;
    }

    public ShoppingCartDto updateCart(Long id, List<ProductsDto> productsDtos) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElse(null);
        if (shoppingCart != null && !shoppingCart.getCheckedOut()) {
            for (ProductsDto productsDto : productsDtos) {
                Product retProduct = productService.findById(productsDto.getProduct_id()).orElse(null);
                if (retProduct != null) {
                    Products products = new Products();
                    products.setCart(shoppingCart);
                    products.setProduct(retProduct);
                    products.setQuantity(productsDto.getQuantity());
                    Products UpdatedProducts = productsRepository.save(products);
                    shoppingCart.getProducts().add(UpdatedProducts);
                }
            }
            ShoppingCart cartEntity = shoppingCartRepository.save(shoppingCart);
            return convertToDto(List.of(cartEntity)).getFirst();
        }
        return null;
    }

    public CheckoutDto checkoutCart(Long id) {
        Optional<ShoppingCart> cartEntity = shoppingCartRepository.findById(id);
        if (cartEntity.isPresent()) {
            cartEntity.get().setCheckedOut(true);
            ShoppingCart updatedCartEntity = shoppingCartRepository.save(cartEntity.get());
            return new CheckoutDto(convertToDto(List.of(updatedCartEntity)).getFirst(), calculateTotalPrice(cartEntity.get().getProducts()));
        }
        return null;
    }

    public static BigDecimal calculateTotalPrice(List<Products> products) {
        return products.stream()
                       .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                       .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}