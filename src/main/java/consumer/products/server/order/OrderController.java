package consumer.products.server.order;

import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private final OrderRepo database;
    private OrderLinkFactory orderLinkFactory;

    public OrderController(OrderRepo database, OrderLinkFactory orderLinkFactory) {
        this.database = database;
        this.orderLinkFactory = orderLinkFactory;
    }

    @GetMapping("/orders")
   Integer allOrders(){
        return 1;
    }


//    // 1
//    @GetMapping("/orders")
//    public ResponseEntity<CollectionModel<EntityModel<Order>>> allOrders(){
//        List<EntityModel<Order>> orders = database.findAll().stream()
//                .map(order -> orderLinkFactory.toModel(order))
//                .collect(Collectors.toList());
//        return ResponseEntity
//                .ok(CollectionModel.of(orders));
//    }


    //     2. in ProductNotFoundException I added a ResponseEntity with status 404.
    @GetMapping("order/{id}")
    ResponseEntity<EntityModel<Order>> singleOrder(@PathVariable Long id) {
        Order order = database.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return ResponseEntity.ok(orderLinkFactory.toModel(order));
    }

    // 3) יצירת הזמנה חדשה
    @PostMapping("/orders")
    // TODO: check why "?"
    ResponseEntity<?> placeOrder(@RequestBody Order order) {
        EntityModel<Order> entityProduct =
                orderLinkFactory.toModel(database.save(order));
        return ResponseEntity
                .created(entityProduct.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityProduct);
    }

}