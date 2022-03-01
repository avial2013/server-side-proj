package consumer.products.server.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProductDAL extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.name = :title")
    Product findByTitle(String title);
}
