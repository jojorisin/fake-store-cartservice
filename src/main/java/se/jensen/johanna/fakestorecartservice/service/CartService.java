package se.jensen.johanna.fakestorecartservice.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import se.jensen.johanna.fakestorecartservice.client.ProductClient;
import se.jensen.johanna.fakestorecartservice.dto.CartItemDTO;
import se.jensen.johanna.fakestorecartservice.dto.CartRequest;
import se.jensen.johanna.fakestorecartservice.dto.CartResponse;
import se.jensen.johanna.fakestorecartservice.dto.MergeRequest;
import se.jensen.johanna.fakestorecartservice.dto.ProductBatchResponse;
import se.jensen.johanna.fakestorecartservice.dto.ProductDTO;
import se.jensen.johanna.fakestorecartservice.exception.InternalClientException;
import se.jensen.johanna.fakestorecartservice.exception.ProductNotFound;
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
  private final ProductClient productClient;


  public CartResponse getCart(Jwt jwt) {
    log.debug("get cart...");
    UUID userId = extractUserId(jwt);

    Cart cart = fetchOrCreateCart(userId);
    Map<UUID, CartItem> cartItemsMap = cart.getCartItemsMap();

    if (cartItemsMap.isEmpty()) {
      return new CartResponse(List.of());
    }

    Set<UUID> productIds = cartItemsMap.keySet();
    List<ProductDTO> products = fetchProducts(productIds);
    if (products.size() != cartItemsMap.size()) {
      throw new ProductNotFound("Not all products were found");
    }
    List<CartItemDTO> cartItemDTOS = products.stream().map(p -> {
      int quantity = cartItemsMap.get(p.productId()).getQuantity();
      return new CartItemDTO(p, quantity);
    }).toList();

    return new CartResponse(cartItemDTOS);
  }


  public void addToCart(Jwt jwt, CartRequest request) {
    log.debug("adding to cart. cartItems:{}", request);
    UUID userId = extractUserId(jwt);
    CartItem cartItem = cartMapper.toCartItem(request);
    Cart cart = fetchOrCreateCart(userId);
    cart.addItem(cartItem);
    cartRepository.save(cart);

   /* if (isGuestSession(jwt, sessionId)) {
      log.debug("adding cartItems to guest-cart...");
      GuestCart guestCart = fetchOrCreateGuestCart(UUID.fromString(sessionId));
      guestCart.addItem(cartItem);
      guestCartRepository.save(guestCart);
    } else {
      log.debug("adding cartItems to cart...");
      Cart cart = fetchOrCreateCart(extractUserId(jwt));
      cart.addItem(cartItem);
      cartRepository.save(cart);
    }*/
  }

  /**
   *
   * @param jwt token for authenticated user
   */
  public void mergeCart(Jwt jwt, MergeRequest request) {
    log.debug("request {}", request);
    try {
      log.debug("merging cart...");
      UUID userId = extractUserId(jwt);
      Cart cart = fetchOrCreateCart(userId);
      cart.mergeCart(request.cartItems().stream().map(cartMapper::toCartItem).toList());
      cartRepository.save(cart);
    } catch (Exception e) {
      log.error("exception", e);
      throw e;
    }

  }


  private Cart fetchOrCreateCart(UUID userId) {
    return cartRepository.findById(userId).orElseGet(() -> {
      log.debug("creating new cart");
      return Cart.createCart(userId);
    });
  }

  private UUID extractUserId(Jwt jwt) {
    return UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
  }


  private List<ProductDTO> fetchProducts(Set<UUID> productIds) {
    ProductBatchResponse batchResponse;
    try {
      batchResponse = productClient.getProductBatch(productIds);
    } catch (RestClientException e) {
      throw new InternalClientException("Unable to fetch products", e);
    }
    if (batchResponse == null || batchResponse.products() == null) {
      log.error(
          "Product service returned null when fetching products. Response: {}, Product ids: {} ",
          batchResponse,
          productIds);
      throw new InternalClientException("Invalid response from product-service");
    }
    if (batchResponse.products().isEmpty()) {
      log.warn("Product service returned empty list when fetching cart cartItems. Product ids: {}",
          productIds);
      throw new ProductNotFound("Products not found.");
    }
    return batchResponse.products();

  }


}
