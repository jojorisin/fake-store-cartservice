package se.jensen.johanna.fakestorecartservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record CartRequest(
    @NotNull(message = "Cart is empty. Please add cartItems.")
    UUID productId,
    @Positive
    @Max(value = 100, message = "Quantity cannot exceed 100. Please contact costumer support.")
    Integer quantity

) {

}
