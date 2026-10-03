package com.rabtech.inventory.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        BigDecimal price,
        Integer stockQuantity,
        Long categoryId,
        String categoryName
) {}