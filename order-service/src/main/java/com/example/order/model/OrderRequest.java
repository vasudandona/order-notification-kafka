package com.example.order.model;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record OrderRequest(
        @NotBlank String customerName,
        @Email @NotBlank String customerEmail,
        @NotBlank String product,
        @Min(1) int quantity,
        @NotNull @Positive BigDecimal price) {
}
