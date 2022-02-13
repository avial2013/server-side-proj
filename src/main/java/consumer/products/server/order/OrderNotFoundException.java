package consumer.products.server.order;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long id) {
        super("Order corresponding to id = " + id + " dose not exist");
    }
}
