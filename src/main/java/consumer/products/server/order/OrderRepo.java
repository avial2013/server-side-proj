package consumer.products.server.order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long >{

    @Query("select o from Order o where o.price < :price")
    List<Order> findOrdersThatTheyCheaper(double price);
}
