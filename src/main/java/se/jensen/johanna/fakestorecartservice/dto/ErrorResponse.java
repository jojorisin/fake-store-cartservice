package se.jensen.johanna.fakestorecartservice.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    Instant timestamp,
    int status,
    String message,
    Map<String, String> fieldErrors) {

}
