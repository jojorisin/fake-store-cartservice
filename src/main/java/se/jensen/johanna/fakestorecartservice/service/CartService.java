package se.jensen.johanna.fakestorecartservice.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.jensen.johanna.fakestorecartservice.dto.CartItemDTO;
import se.jensen.johanna.fakestorecartservice.dto.CartRequest;
import se.jensen.johanna.fakestorecartservice.dto.CartResponse;
import se.jensen.johanna.fakestorecartservice.mapper.CartMapper;
import se.jensen.johanna.fakestorecartservice.model.Cart;
import se.jensen.johanna.fakestorecartservice.repository.CartRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

  private final CartRepository cartRepository;
  private final CartMapper cartMapper;

  @Transactional
  public CartResponse getCart(Jwt jwt, String sessionId) {
    Cart cartToReturn = getOrCreateCart(jwt, sessionId).orElseThrow(() -> {
      log.error("no cartId found");
      return new RuntimeException("no cartId found");
    });
    List<CartItemDTO> cartItemDTOS = cartToReturn.getCartItems().stream()
        .map(cartMapper::toCartItemDTO)
        .toList();

    return new CartResponse(cartItemDTOS);
  }


  @Transactional
  public void addToCart(Jwt jwt, String sessionId, CartRequest cartRequest) {
    Cart cart = getOrCreateCart(jwt, sessionId).orElseThrow(() -> {
      log.error("no cartId found");
      return new RuntimeException("no cartId found");
    });
    cart.addItem(cartMapper.toCartItem(cartRequest));
    cartRepository.save(cart);
  }

  @Transactional
  public Optional<Cart> getOrCreateCart(Jwt jwt, String sessionId) {
    String userId = jwt != null ? jwt.getSubject() : null;
    String guestId = sessionId != null && !sessionId.isBlank() ? sessionId : null;

    Cart cartToReturn;
    if (userId != null && guestId != null) {
      cartToReturn = mergeCart(jwt, sessionId);
    } else if (userId != null) {
      cartToReturn = cartRepository.findById(userId).orElseGet(() -> Cart.createCart(userId));
    } else if (guestId != null) {
      cartToReturn = cartRepository.findById(guestId).orElseGet(() -> Cart.createCart(guestId));
    } else {
      return Optional.empty();
    }
    return Optional.of(cartToReturn);
  }


  @Transactional
  public Cart mergeCart(Jwt jwt, String sessionId) {
    String userId = Objects.requireNonNull(jwt.getSubject());

    Cart userCart = cartRepository.findById(userId)
        .orElseGet(() -> Cart.createCart(jwt.getSubject()));

    if (sessionId == null || sessionId.isBlank()) {
      return userCart;
    }

    cartRepository.findById(sessionId).ifPresent(guestCart -> {
      userCart.mergeCart(guestCart.getCartItems());
      cartRepository.save(userCart);
      cartRepository.deleteById(sessionId);
    });

    return userCart;
  }

}
