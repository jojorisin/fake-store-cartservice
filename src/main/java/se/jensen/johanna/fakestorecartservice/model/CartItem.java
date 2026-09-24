package se.jensen.johanna.fakestorecartservice.model;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import se.jensen.johanna.fakestorecartservice.exception.IllegalCartStateException;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItem {

  private UUID productId;
  private int quantity;

  public static CartItem create(UUID productId, int quantity) {
    validateQuantity(quantity);
    if (productId == null) {
      throw new IllegalCartStateException("Missing product id.");
    }
    return CartItem.builder().productId(productId).quantity(quantity).build();

  }

  public void updateQuantity(int quantity) {
    validateQuantity(quantity);
    this.quantity = quantity;
  }

  private static void validateQuantity(int quantity) {
    if (quantity < 0 || quantity > 100) {
      throw new IllegalCartStateException("Quantity must be between 1-100.");
    }

  }

}
