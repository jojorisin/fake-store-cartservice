package se.jensen.johanna.fakestorecartservice.service;

import java.util.List;
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
import se.jensen.johanna.fakestorecartservice.model.CartItem;
import se.jensen.johanna.fakestorecartservice.repository.CartRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

  private final CartRepository cartRepository;
  private final CartMapper cartMapper;

  @Transactional
  public CartResponse getCart(Jwt jwt, String sessionId) {
    Cart cart;
    boolean needsMerge = jwt != null && sessionId != null;
    if (needsMerge) {
      cart = mergeAndGetCart(jwt, sessionId);
    } else {
      String activeSession = jwt != null ? jwt.getSubject() : sessionId;
      if (activeSession == null) {
        log.debug("controller returned null session id.");
        throw new IllegalStateException("No session id found");
      }
      cart = cartRepository.findById(activeSession)
          .orElse(Cart.createCart(activeSession));
    }
    cartRepository.save(cart);
    List<CartItemDTO> cartItemDTOS = cart.getCartItems().stream().map(cartMapper::toCartItemDTO)
        .toList();

    return new CartResponse(cartItemDTOS);
  }

  @Transactional
  public void addToCart(Jwt jwt, String sessionId, CartRequest cartRequest) {
    // if the user has logged in, update the cart-key to userid.
    log.debug("adding item to cart {}", cartRequest);
    Cart cart;
    boolean needsMerge = jwt != null && sessionId != null;
    if (needsMerge) {
      cart = mergeAndGetCart(jwt, sessionId);
    } else {
      sessionId = jwt != null ? jwt.getSubject() : sessionId;
      log.debug("jwt is null, sessionId is {}", sessionId);
      if (sessionId == null) {
        //controller should have produced new sessionId
        log.error("sessionId is null");
        throw new IllegalStateException("No session id found");
      }
      cart = cartRepository.findById(sessionId).orElse(Cart.createCart(sessionId));
    }
    CartItem item = cartMapper.toCartItem(cartRequest);
    cart.addItem(item);
    cartRepository.save(cart);
  }


  private Cart mergeAndGetCart(Jwt jwt, String sessionId) {
    log.debug("merging cart for user {}", jwt.getSubject());
    Cart cart = cartRepository.findById(sessionId).orElse(Cart.createCart(sessionId));
    cart.mergeCart(jwt.getSubject());
    cartRepository.save(cart);
    log.debug("successfully merged cart for user {}", jwt.getSubject());
    return cart;
  }

}
