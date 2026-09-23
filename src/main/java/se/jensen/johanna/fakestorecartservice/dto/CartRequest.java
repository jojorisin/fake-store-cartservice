package se.jensen.johanna.fakestorecartservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record CartRequest(
    @NotNull(message = "Cart is empty. Please add cartItems.")
    UUID productId,
    @Positive
    Integer quantity

) {

}
