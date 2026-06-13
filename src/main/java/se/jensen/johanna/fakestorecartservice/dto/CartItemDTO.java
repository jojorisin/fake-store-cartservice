package se.jensen.johanna.fakestorecartservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemDTO(
    UUID productId,
    Integer quantity,
    BigDecimal price
) {


}
