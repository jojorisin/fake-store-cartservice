package se.jensen.johanna.fakestorecartservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record CartRequest(
    @NotNull(message = "Cart is empty. Please add items.")
    UUID productId,
    @Positive
    Integer quantity,
    @Positive
    BigDecimal price
) {

}
