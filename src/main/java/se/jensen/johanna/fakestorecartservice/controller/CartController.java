package se.jensen.johanna.fakestorecartservice.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.johanna.fakestorecartservice.controller.webutils.CookieFactory;
import se.jensen.johanna.fakestorecartservice.dto.CartRequest;
import se.jensen.johanna.fakestorecartservice.service.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

  private final CookieFactory cookieFactory;
  private final CartService cartService;

  // endpoint to save cart?

  // optional jwt? when checkingout it's needed. add findby cartsession..?
  @GetMapping
  public ResponseEntity<Void> getCart(
      @AuthenticationPrincipal Jwt jwt,
      @CookieValue(value = "cartSessionId", required = false) String sessionId) {
    boolean isNewSession = jwt == null && sessionId == null;
    if (isNewSession) {
      sessionId = UUID.randomUUID().toString();
    }

    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<Void> addToCart(
      @AuthenticationPrincipal Jwt jwt,
      @CookieValue(value = "cartSessionId", required = false) String sessionId,
      @RequestBody CartRequest cartRequest) {

    boolean isNewSession = jwt == null && sessionId == null;
    if (isNewSession) {
      sessionId = UUID.randomUUID().toString();
    }
    cartService.addToCart(jwt, sessionId, cartRequest);
    ResponseCookie cartSessionCookie = resolveCookie(isNewSession, jwt, sessionId);
    if (cartSessionCookie != null) {
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cartSessionCookie.toString())
          .build();
    }
    return ResponseEntity.ok().build();
  }


  private ResponseCookie resolveCookie(boolean isNewSession, Jwt jwt, String sessionId) {
    boolean isMerging = jwt != null && sessionId != null;

    if (isNewSession) {
      return cookieFactory.createCartSessionCookie(sessionId);
    }
    if (isMerging) {
      return cookieFactory.getCleanCartSessionCookie();
    }
    return null;


  }


}
