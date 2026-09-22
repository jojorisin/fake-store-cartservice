package se.jensen.johanna.fakestorecartservice.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import se.jensen.johanna.fakestorecartservice.model.Cart;

public interface CartRepository extends CrudRepository<Cart, UUID> {

  public boolean existsByUserId(UUID userId);

}
