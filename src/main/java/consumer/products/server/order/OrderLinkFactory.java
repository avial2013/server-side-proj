package consumer.products.server.order;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class OrderLinkFactory implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel(Order order) {
        // TODO: להוציא מהערה כשיש את המתדות - singleOrder ו getAllOrders בקונטרולר
        return null;
//        return EntityModel.of(order,
//                linkTo(methodOn(OrderController.class).singleOrder(order.getId())).withSelfRel(),
//                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("All orders"));
    }

    @Override
    public CollectionModel<EntityModel<Order>> toCollectionModel(Iterable<? extends Order> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
