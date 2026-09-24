package se.jensen.johanna.fakestorecartservice.dto;

import java.util.Set;

public record AvailabilityRequest(
    Set<CartRequest> cartItemRequests
) {

}
