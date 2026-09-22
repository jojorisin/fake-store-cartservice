package se.jensen.johanna.fakestorecartservice.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItem {

  private UUID productId;
  private Integer quantity;


  public static CartItem createCartItem(UUID productId, Integer quantity) {
    return CartItem.builder().productId(productId).quantity(quantity).build();
  }

}
