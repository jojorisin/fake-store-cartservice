package se.jensen.johanna.fakestorecartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.johanna.fakestorecartservice.dto.CartRequest;
import se.jensen.johanna.fakestorecartservice.dto.CartResponse;
import se.jensen.johanna.fakestorecartservice.service.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {


  private final CartService cartService;

  // endpoint to save cart?

  // optional jwt? when checkingout it's needed. add findby cartsession..?
  @GetMapping
  public ResponseEntity<CartResponse> getCart(
      @AuthenticationPrincipal Jwt jwt,
      @RequestHeader(value = "cart-session-id", required = false) String sessionId) {

    return ResponseEntity.ok(cartService.getCart(jwt, sessionId));
  }

  @PostMapping
  public ResponseEntity<Void> addToCart(
      @AuthenticationPrincipal Jwt jwt,
      @RequestHeader(value = "cart-session-id", required = false) String sessionId,
      @RequestBody CartRequest cartRequest) {
    cartService.addToCart(jwt, sessionId, cartRequest);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/merge")
  public ResponseEntity<Void> mergeCart(@AuthenticationPrincipal Jwt jwt,
      @RequestHeader(value = "cart-session-id", required = false) String cartSessionId) {
    cartService.mergeCart(jwt, cartSessionId);
    return ResponseEntity.ok().build();
  }


}
