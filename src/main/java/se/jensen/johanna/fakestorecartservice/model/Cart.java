package se.jensen.johanna.fakestorecartservice.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "cart", timeToLive = 604800)
@Builder
@Getter
public class Cart {

  @Id
  private UUID userId;
  private Map<UUID, CartItem> cartItemsMap;

  public void addItem(CartItem newItem) {
    cartItemsMap.compute(newItem.getProductId(), (productId, existingItem) -> {
      if (existingItem != null) {
        existingItem.setQuantity(newItem.getQuantity());
        return existingItem;
      }
      return newItem;
    });

  }

  public void removeItem(UUID productId) {
    cartItemsMap.remove(productId);
  }

  public static Cart createCart(UUID sessionId) {
    return Cart.builder().userId(sessionId).cartItemsMap(new HashMap<>()).build();
  }

  public void mergeCart(List<CartItem> itemsToMerge) {
    itemsToMerge.forEach(this::addItem);
  }
}
