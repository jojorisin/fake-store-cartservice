package se.jensen.johanna.fakestorecartservice.dto;

import java.util.Set;

public record AvailabilityResponse(
    Set<CartRequest> updatedCart,
    Boolean allAvailable
) {

}
