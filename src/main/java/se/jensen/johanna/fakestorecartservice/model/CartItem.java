package se.jensen.johanna.fakestorecartservice.model;

import java.math.BigDecimal;
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
  private BigDecimal price;

  public static CartItem createCartItem(UUID productId, Integer quantity, BigDecimal price) {
    return CartItem.builder().productId(productId).quantity(quantity).price(price).build();
  }

}
