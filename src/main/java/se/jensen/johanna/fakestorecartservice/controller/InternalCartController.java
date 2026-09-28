package se.jensen.johanna.fakestorecartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.johanna.fakestorecartservice.dto.CheckoutCartResponse;
import se.jensen.johanna.fakestorecartservice.service.CartService;

@RestController
@RequestMapping("/api/internal/cart")
@RequiredArgsConstructor
public class InternalCartController {
  private final CartService cartService;

  @GetMapping("/checkout-cart")
  public ResponseEntity<CheckoutCartResponse> getCartForCheckout(@AuthenticationPrincipal Jwt jwt){
    return ResponseEntity.ok().body(cartService.getCartForCheckout(jwt));
  }


}
