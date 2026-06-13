package se.jensen.johanna.fakestorecartservice.controller.webutils;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieFactory {

  @Value("${cart.cookie.same.site}")
  private String sameSite;

  @Value("${cart.cookie.secure}")
  private boolean cookieSecure;

  public ResponseCookie createCartSessionCookie(String sessionId) {
    return ResponseCookie.from("cartSessionId", sessionId)
        .maxAge(Duration.ofDays(30)).path("/").
        sameSite(sameSite).secure(cookieSecure).httpOnly(true).build();
  }

  public ResponseCookie getCleanCartSessionCookie() {
    return ResponseCookie.from("cartSessionId", "").maxAge(Duration.ofDays(0)).path("/").
        sameSite(sameSite).secure(cookieSecure).httpOnly(true).build();
  }


}
