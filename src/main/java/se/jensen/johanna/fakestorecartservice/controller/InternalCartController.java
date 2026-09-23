package se.jensen.johanna.fakestorecartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.johanna.fakestorecartservice.service.CartService;

@RestController
@RequestMapping("/api/internal/cart")
@RequiredArgsConstructor
public class InternalCartController {

  private final CartService cartService;


}
