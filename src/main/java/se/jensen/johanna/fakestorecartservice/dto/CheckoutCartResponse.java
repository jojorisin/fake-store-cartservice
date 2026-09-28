package se.jensen.johanna.fakestorecartservice.dto;

import java.util.List;

public record CheckoutCartResponse(
    List<CheckoutCartItemDTO> checkoutCart
) {

}
