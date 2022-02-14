package consumer.products.server.order;

import consumer.products.server.product.Product;
import consumer.products.server.product.ProductController;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {

    private final OrderRepo database;
    private OrderLinkFactory orderLinkFactory;

    public OrderController(OrderRepo database, OrderLinkFactory orderLinkFactory) {
        this.database = database;
        this.orderLinkFactory = orderLinkFactory;
    }


    // 1
    @GetMapping("/orders")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> allOrders() {
        List<EntityModel<Order>> orders = database.findAll().stream()
                .map(order -> orderLinkFactory.toModel(order))
                .collect(Collectors.toList());
        return ResponseEntity
                .ok(CollectionModel.of(orders));
    }


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

    @GetMapping("/orders/{id}/products")
    public ResponseEntity<CollectionModel<EntityModel<Product>>>
    productsByOrder(@PathVariable long id) {
        // מצאתי את ההזמנה לפי המזהה ואם היא לא קיימת תיזק שגיאה
        Order order = database.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // לקחתי את כל מוצרים שקשורים לאותה הזמנה
        List<Product> products = order.getProductList();

        // EntityModel הפכתי אותו ל
        // והופסתי קישור לאותו מוצר ואותה הזמנה
        List<EntityModel<Product>> entityModelList = products.stream()
                .map(product ->
                        orderLinkFactory.toModelOfProductWithOrderLink(order, product)
                )
                .collect(Collectors.toList());

        // הוספתי סטאטוס 200
        return ResponseEntity.ok(CollectionModel.of(entityModelList));
    }

    @GetMapping("/orders/{id}/sale")
    public ResponseEntity<CollectionModel<EntityModel<Product>>>
    productsByOrderWithSale(@PathVariable long id) {
        // מצאתי את ההזמנה לפי המזהה ואם היא לא קיימת תיזק שגיאה
        Order order = database.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // יצרתי רשימה חדשה עם האובייקטים חדשים שהם אותו מוצר רק עם מחיר אחרי הנחה
        List<Product> productsAfterSale = new LinkedList<>();
        for (Product product : order.getProductList()) {
            Product temp = new Product(product);
            temp.setPrice(temp.getPrice() * 0.75);
            productsAfterSale.add(temp);
        }
        // EntityModel הפכתי אותו ל
        // והופסתי קישור לאותו מוצר ואותה הזמנה
        List<EntityModel<Product>> entityModelList = productsAfterSale.stream()
                .map(product ->
                        orderLinkFactory.toModelOfProductWithOrderLink(order, product)
                )
                .collect(Collectors.toList());

        // הוספתי סטאטוס 200
        return ResponseEntity.ok(CollectionModel.of(entityModelList));
    }
    
    // שאלה 4 ב - OrderController
    @PutMapping("order/{id}")
    ResponseEntity<?> addToOrder(@RequestBody Product aProduct, @PathVariable Long orderID) {

        // הכנסתי את ה-ORDER למשתנה ע"י הID- שלו
        Order updatedOrder = database.getById(orderID);
        // הוספתי ל-LIST את ה-PRODUCT מה-REQUEST_BODY
        updatedOrder.getProductList().add(aProduct);

        // שלחתי בחזרה את ה-RESPONSE_ENTITY שלו
        EntityModel<Order> entityOrder = orderLinkFactory.toModel(database.save(updatedOrder));
        return ResponseEntity.created(entityOrder.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityOrder);
    }
}
