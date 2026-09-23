package se.jensen.johanna.fakestorecartservice.dto;

import java.util.List;

public record ProductBatchResponse(
    List<ProductDTO> products
) {

}
