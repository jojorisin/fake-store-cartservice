package se.jensen.johanna.fakestorecartservice.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
    boolean isUpdated = false;

    // creating empty cart if user doesn't have one already
    Cart cart = fetchOrCreateCart(userId);
    Map<UUID, CartItem> cartItemsMap = cart.getCartItemsMap();

    if (cartItemsMap.isEmpty()) {
      log.debug("returning empty cart");
      return new CartResponse(List.of(), isUpdated);
    }

    // fetching cart items from products with updated price and info
    Set<UUID> productIds = cartItemsMap.keySet();
    List<ProductDTO> products = fetchProducts(productIds);

    // removing items no longer in product-catalog
    // all cart items are validated in addToCart
    // for old carts that could however not be sufficient
    if (products.size() != cartItemsMap.size()) {
      log.debug("unable to validate all ids. request {}, response: {}", cartItemsMap, products);
      List<UUID> invalidItems = cartItemsMap.keySet().stream()
          .filter(id -> products.stream().noneMatch(
              p -> p.productId().equals(id))).toList();

      invalidItems.forEach(cart::removeItem);
      cartRepository.save(cart);
      isUpdated = true;
    }
    // returning only validated existing items with current price and info
    List<CartItemDTO> cartItemDTOS = products.stream().map(p -> {
      int quantity = cartItemsMap.get(p.productId()).getQuantity();
      return new CartItemDTO(p, quantity);
    }).toList();

    return new CartResponse(cartItemDTOS, isUpdated);
  }

  /**
   * Validates product exists and adds to cart
   *
   * @param jwt
   * @param request
   */
  public void addToCart(Jwt jwt, CartRequest request) {
    log.debug("adding to cart. cartItems:{}", request);
    validateProduct(request.productId());
    UUID userId = extractUserId(jwt);
    CartItem cartItem = cartMapper.toCartItem(request);
    Cart cart = fetchOrCreateCart(userId);
    cart.addItem(cartItem);
    cartRepository.save(cart);

  }

  /**
   * Checks product id exists in product catalog
   */
  private void validateProduct(UUID productId) {
    Boolean productExists;
    try {
      productExists = productClient.productExists(productId);
    } catch (RestClientException e) {
      throw new InternalClientException("Unable to process request", e);
    }
    if (productExists == null || !productExists) {
      throw new ProductNotFound("Product not found.");
    }

  }

  /**
   * Merges items from guest-cart in local storage to users cart Validates request contains valid
   * id's in product catalog
   *
   * @param jwt token for authenticated user
   */
  public void mergeCart(Jwt jwt, MergeRequest request) {
    log.debug("merging cart. request {}.", request);
    List<CartRequest> requestedItems = request.cartItems();
    if (requestedItems == null || requestedItems.isEmpty()) {
      return;
    }

    // validate id's in product catalog.
    // invalid ids are ignored
    Set<UUID> idsToValidate = requestedItems.stream().map(CartRequest::productId)
        .collect(Collectors.toSet());
    Set<UUID> validatedProducts = validateCartProducts(idsToValidate);
    if (validatedProducts.isEmpty()) {
      return;
    }
    if (idsToValidate.size() != validatedProducts.size()) {
      log.debug("unable to validate all products in merge request");
    }
    UUID userId = extractUserId(jwt);
    Cart cart = fetchOrCreateCart(userId);
    // save only validated items to cart
    List<CartItem> itemsToMerge = requestedItems.stream().filter(
            i -> validatedProducts.contains(i.productId()))
        .map(cartMapper::toCartItem).toList();

    cart.mergeCart(itemsToMerge);
    cartRepository.save(cart);

  }

  /**
   * validates set of ids exists in product catalog returns only existing ids
   */
  private Set<UUID> validateCartProducts(Set<UUID> productIds) {
    Set<UUID> existingProductIds;
    try {
      existingProductIds = productClient.validateExistingProducts(productIds);
      return existingProductIds != null ? existingProductIds : Collections.emptySet();
    } catch (RestClientException e) {
      throw new InternalClientException("Unable to process request", e);
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

  /**
   * fetches products with detailed info and price from catalog
   */
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
