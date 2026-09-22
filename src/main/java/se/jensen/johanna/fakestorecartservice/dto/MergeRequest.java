package se.jensen.johanna.fakestorecartservice.dto;

import java.util.List;

public record MergeRequest(
    List<CartRequest> cartItems
) {

}
