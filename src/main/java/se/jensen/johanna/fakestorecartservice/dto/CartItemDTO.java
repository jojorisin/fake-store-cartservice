package se.jensen.johanna.fakestorecartservice.dto;

public record CartItemDTO(
    ProductDTO product,
    Integer quantity
) {


}
