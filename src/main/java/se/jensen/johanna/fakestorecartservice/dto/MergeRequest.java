package se.jensen.johanna.fakestorecartservice.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record MergeRequest(
    @NotNull List<@NotNull CartRequest> cartItems
) {

}
