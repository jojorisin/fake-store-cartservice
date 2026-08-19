package se.jensen.johanna.fakestorecartservice.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("cart")
@Builder
@Getter
public class Cart {

  @Id
  private String cartId;
  private List<CartItem> cartItems;

  public void addItem(CartItem newItem) {

    cartItems.stream().filter(i -> i.getProductId().equals(newItem.getProductId()))
        .findFirst()
        .ifPresentOrElse(i -> i.setQuantity(newItem.getQuantity()), () -> cartItems.add(newItem));

  }

  public static Cart createCart(String sessionId) {
    return Cart.builder().cartId(sessionId).cartItems(new ArrayList<>()).build();
  }

  public void mergeCart(List<CartItem> itemsToMerge) {
    cartItems.addAll(itemsToMerge);
  }
}
