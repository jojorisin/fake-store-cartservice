package se.jensen.johanna.fakestorecartservice.dto;

import java.util.UUID;

public record ProductDTO(
    UUID productId,
    String title,
    Integer price,
    String description,
    String image
) {

}

