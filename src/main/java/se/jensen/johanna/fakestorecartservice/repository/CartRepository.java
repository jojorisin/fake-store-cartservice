package se.jensen.johanna.fakestorecartservice.repository;

import org.springframework.data.repository.CrudRepository;
import se.jensen.johanna.fakestorecartservice.model.Cart;

public interface CartRepository extends CrudRepository<Cart, String> {

  public boolean existsByCartId(String cartId);

}
