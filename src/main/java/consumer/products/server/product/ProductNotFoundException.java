package consumer.products.server.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product corresponding to id = " + id + " does not exist" + ResponseEntity.notFound());
    }
 
}
