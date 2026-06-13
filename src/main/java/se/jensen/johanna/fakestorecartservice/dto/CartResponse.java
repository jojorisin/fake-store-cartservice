package se.jensen.johanna.fakestorecartservice.dto;

import java.util.List;

public record CartResponse(
    List<CartItemDTO> cartItems
) {


}
