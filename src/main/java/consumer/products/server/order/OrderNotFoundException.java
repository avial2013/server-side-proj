package consumer.products.server.order;

import org.springframework.http.ResponseEntity;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long id) {
        super("Order corresponding to id = " + id + " dose not exist" + ResponseEntity.notFound());
    }

}
