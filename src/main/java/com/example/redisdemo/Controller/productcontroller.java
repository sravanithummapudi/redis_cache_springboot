package com.example.redisdemo.Controller;

import com.example.redisdemo.ProductService.ProductService;
import com.example.redisdemo.model.product;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.cache.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
//Defines cache-related configurations at the class level.
//You don’t need to repeat cacheNames in every method. already given for product covered for every method
@CacheConfig(cacheNames = "product")
public class productcontroller {
    private final ProductService productService;


    public productcontroller(ProductService productService) {
        this.productService = productService;
    }

    // 1. Get product by id with caching
    @GetMapping("/product/{id}")
    @Cacheable(value = "product", keyGenerator = "customKeyGenerator")
    //Caches the result of a method call. If the method is called again
    // with the same parameters, the result is returned from the cache
    // instead of executing the method
    public product getProductById(@PathVariable long id) {
        return productService.getProductById(id);
    }


    // Example: Get all products (cache all entries)
    @GetMapping
    @Cacheable(cacheNames = "productList")  // Separate cache for list
    public List<product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 2. Update product & update cache for this product, evict product list cache
    @PutMapping("/product/{id}")
    //Allows multiple cache annotations on the same method.
    @Caching(
            //Always executes the method and updates the cache with the result.
            put = @CachePut(key = "#id"),
            //Removes one or more cache entries.
            evict = @CacheEvict(value = "productList", allEntries = true)
    )
    public product editProduct(@PathVariable long id, @RequestBody product product) {
        return productService.updateProduct(id, product);
    }

    // 3. Delete product and evict cache entry for that product
    @DeleteMapping("/product/{id}")
    // beforeinvocation Removes the cache entry before method executes (default is false, i.e., after).
    @CacheEvict(key = "#id", beforeInvocation = true)
    public String removeProductById(@PathVariable long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }

    @PostMapping("/product")
    public product addProduct(@RequestBody product product) {
        return productService.addProduct(product);
    }

}
