package se.jensen.johanna.fakestorecartservice.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import se.jensen.johanna.fakestorecartservice.exception.IllegalCartStateException;

@RedisHash(value = "cart", timeToLive = 604800)
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Cart {

  @Id
  private UUID userId;
  private Map<UUID, CartItem> cartItemsMap;

  /**
   * Adds item to cart. New quantity replaces old one
   */
  public void addItem(CartItem newItem) {
    validateCartCapacity(newItem.getProductId(), newItem.getQuantity());
    cartItemsMap.compute(newItem.getProductId(), (productId, existingItem) -> {
      if (existingItem != null) {
        existingItem.updateQuantity(newItem.getQuantity());
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

  public void updateQuantity(UUID productId, int quantity) {
    if (quantity > 0) {
      validateCartCapacity(productId, quantity);
    }
    cartItemsMap.computeIfPresent(productId, (id, itemToUpdate) -> {
      if (quantity <= 0) {
        return null;
      }
      itemToUpdate.updateQuantity(quantity);

      return itemToUpdate;
    });

  }

  public void mergeCart(List<CartItem> itemsToMerge) {
    itemsToMerge.forEach(this::addItem);
  }

  private int getTotalQuantity() {
    return cartItemsMap.values().stream().mapToInt(CartItem::getQuantity).sum();
  }

  private void validateCartCapacity(UUID productId, int requestedQuantity) {
    int existingQuantity = cartItemsMap.containsKey(productId) ?
        cartItemsMap.get(productId).getQuantity() : 0;
    int expectedTotal = getTotalQuantity() - existingQuantity + requestedQuantity;
    if (expectedTotal > 100) {
      throw new IllegalCartStateException("You have exceeded 100 items in cart.");
    }

  }
}
