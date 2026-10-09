package com.rabtech.inventory.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";

    public AuthResponseDTO(String token) {
        this.token = token;
    }
}