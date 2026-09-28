package se.jensen.johanna.fakestorecartservice.mapper;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.jensen.johanna.fakestorecartservice.dto.CartRequest;
import se.jensen.johanna.fakestorecartservice.dto.CheckoutCartItemDTO;
import se.jensen.johanna.fakestorecartservice.model.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {

  default Set<CartRequest> toSetCartRequest(Map<UUID, CartItem> cartItemMap) {
    return toCartRequestSet(cartItemMap.values());
  }

  Set<CartRequest> toCartRequestSet(Collection<CartItem> cartItems);


  CartRequest toCartRequest(CartItem cartItem);

  @Mapping(target = "pricePerItem", source = "price")
  CheckoutCartItemDTO toCheckoutCartItem(CartItem cartItem, BigDecimal price, String title);


}
