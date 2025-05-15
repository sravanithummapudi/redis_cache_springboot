package com.example.redisdemo.ProductService;

import com.example.redisdemo.Repository.productrepository;
import com.example.redisdemo.model.product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ProductService {
    private final productrepository productRepository;

    public ProductService(productrepository productRepository) {
        this.productRepository = productRepository;
    }
    public product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    public List<product> getAllProducts() {
        return productRepository.findAll();
    }

    public product updateProduct(long id, product productFromRequest) {
        product existingProduct = getProductById(id);

        // Update fields
        existingProduct.setName(productFromRequest.getName());
        existingProduct.setCode(productFromRequest.getCode());
        existingProduct.setQuantity(productFromRequest.getQuantity());
        existingProduct.setPrice(productFromRequest.getPrice());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(long id) {
        productRepository.deleteById(id);

    }

    public product addProduct(product product) {
        return productRepository.save(product);
    }
}
