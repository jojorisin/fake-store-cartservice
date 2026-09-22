package se.jensen.johanna.fakestorecartservice.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "cart:user")
@Builder
@Getter
public class Cart {

  @Id
  private UUID userId;
  private Map<UUID, CartItem> cartItemsMap;
  //private List<CartItem> cartItems;

  public void addItem(CartItem newItem) {
    cartItemsMap.compute(newItem.getProductId(), (productId, existingItem) -> {
      if (existingItem != null) {
        existingItem.setQuantity(newItem.getQuantity());
        return existingItem;
      }
      return newItem;
    });

   /* cartItems.stream().filter(i -> i.getProductId().equals(newItem.getProductId()))
        .findFirst()
        .ifPresentOrElse(i -> i.setQuantity(newItem.getQuantity()), () -> cartItems.add(newItem));*/

  }


  public static Cart createCart(UUID sessionId) {
    return Cart.builder().userId(sessionId).cartItemsMap(new HashMap<>()).build();
  }

  public void mergeCart(List<CartItem> itemsToMerge) {
    itemsToMerge.forEach(this::addItem);
    //cartItems.addAll(itemsToMerge);
  }
}
