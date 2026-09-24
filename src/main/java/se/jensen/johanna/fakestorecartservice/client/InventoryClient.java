package se.jensen.johanna.fakestorecartservice.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import se.jensen.johanna.fakestorecartservice.dto.AvailabilityRequest;
import se.jensen.johanna.fakestorecartservice.dto.AvailabilityResponse;

public interface InventoryClient {

  @PostExchange("/inventory/check-stock")
  AvailabilityResponse checkStock(@RequestBody AvailabilityRequest request);

}
