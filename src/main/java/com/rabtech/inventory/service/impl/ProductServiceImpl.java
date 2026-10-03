package com.rabtech.inventory.service.impl;

import com.rabtech.inventory.dto.ProductRequestDTO;
import com.rabtech.inventory.dto.ProductResponseDTO;
import com.rabtech.inventory.entity.Category;
import com.rabtech.inventory.entity.Product;
import com.rabtech.inventory.exception.ResourceNotFoundException;
import com.rabtech.inventory.repository.CategoryRepository;
import com.rabtech.inventory.repository.ProductRepository;
import com.rabtech.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        Category category = categoryRepository.findById(requestDTO.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDTO.categoryId()));

        Product product = new Product();
        product.setName(requestDTO.name());
        product.setPrice(requestDTO.price());
        product.setStockQuantity(requestDTO.stockQuantity());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return mapToResponseDTO(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(requestDTO.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDTO.categoryId()));

        product.setName(requestDTO.name());
        product.setPrice(requestDTO.price());
        product.setStockQuantity(requestDTO.stockQuantity());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return mapToResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}