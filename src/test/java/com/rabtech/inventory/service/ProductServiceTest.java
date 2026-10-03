package com.rabtech.inventory.service;

import com.rabtech.inventory.dto.ProductRequestDTO;
import com.rabtech.inventory.dto.ProductResponseDTO;
import com.rabtech.inventory.entity.Category;
import com.rabtech.inventory.entity.Product;
import com.rabtech.inventory.repository.CategoryRepository;
import com.rabtech.inventory.repository.ProductRepository;
import com.rabtech.inventory.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCreateProduct_Success() {
        Category category = new Category(1L, "Electronics", "Gadgets", null);
        Product product = new Product(1L, "Laptop", new BigDecimal("999.99"), 10, category);

        ProductRequestDTO requestDTO = new ProductRequestDTO("Laptop", new BigDecimal("999.99"), 10, 1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO responseDTO = productService.createProduct(requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Laptop", responseDTO.name());
        verify(productRepository, times(1)).save(any(Product.class));
    }
}