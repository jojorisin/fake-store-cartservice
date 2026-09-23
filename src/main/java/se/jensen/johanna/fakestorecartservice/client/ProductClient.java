package se.jensen.johanna.fakestorecartservice.client;

import java.util.Set;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import se.jensen.johanna.fakestorecartservice.dto.ProductBatchResponse;

public interface ProductClient {

  @PostExchange("/internal/products/batch")
  ProductBatchResponse getProductBatch(@RequestBody Set<UUID> productIds);

  @GetExchange("/internal/products/{productId}/validate")
  Boolean productExists(@PathVariable UUID productId);

  @PostExchange("/internal/products/validate")
  Set<UUID> validateExistingProducts(@RequestBody Set<UUID> productIds);
}
