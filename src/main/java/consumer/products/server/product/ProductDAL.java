package consumer.products.server.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDAL extends JpaRepository<Product,Long> {

}
