package consumer.products.server.product;

import org.springframework.http.ResponseEntity;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product corresponding to id = " + id + " does not exist");
    }

    public ProductNotFoundException(String name) {
        super("Product corresponding to name = '" + name + "' does not exist");
    }
 
}
