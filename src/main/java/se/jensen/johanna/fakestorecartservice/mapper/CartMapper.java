package se.jensen.johanna.fakestorecartservice.mapper;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import se.jensen.johanna.fakestorecartservice.dto.CartRequest;
import se.jensen.johanna.fakestorecartservice.model.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {

  default Set<CartRequest> toSetCartRequest(Map<UUID, CartItem> cartItemMap) {
    return toCartRequestSet(cartItemMap.values());
  }

  Set<CartRequest> toCartRequestSet(Collection<CartItem> cartItems);


  CartRequest toCartRequest(CartItem cartItem);


}
