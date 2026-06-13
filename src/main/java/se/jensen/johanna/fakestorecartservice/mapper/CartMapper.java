package se.jensen.johanna.fakestorecartservice.mapper;

import org.mapstruct.Mapper;
import se.jensen.johanna.fakestorecartservice.dto.CartItemDTO;
import se.jensen.johanna.fakestorecartservice.dto.CartRequest;
import se.jensen.johanna.fakestorecartservice.model.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {

  CartItemDTO toCartItemDTO(CartItem cartItem);

  CartItem toCartItem(CartRequest cartRequest);


}
