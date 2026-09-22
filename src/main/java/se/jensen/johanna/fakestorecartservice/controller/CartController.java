package se.jensen.johanna.fakestorecartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.johanna.fakestorecartservice.dto.CartRequest;
import se.jensen.johanna.fakestorecartservice.dto.CartResponse;
import se.jensen.johanna.fakestorecartservice.dto.MergeRequest;
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
      @AuthenticationPrincipal Jwt jwt) {

    return ResponseEntity.ok(cartService.getCart(jwt));
  }

  @PostMapping
  public ResponseEntity<Void> addToCart(
      @AuthenticationPrincipal Jwt jwt, @RequestBody CartRequest cartRequest) {
    cartService.addToCart(jwt, cartRequest);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/merge")
  public ResponseEntity<Void> mergeCart(@AuthenticationPrincipal Jwt jwt,
      @RequestBody MergeRequest request) {
    cartService.mergeCart(jwt, request);
    return ResponseEntity.ok().build();
  }


}
