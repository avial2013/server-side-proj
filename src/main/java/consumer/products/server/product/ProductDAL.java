package consumer.products.server.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

public interface ProductDAL extends JpaRepository<Product, Long> {

    //    default ResponseEntity<?> findByTitle(String title){
//         List<Product> a = findAll().stream().filter(product -> product.getName().equals(title)).collect(Collectors.toList());
//         if (a.size() == 0)
//            throw new ProductNotFoundException(title);
//         return ResponseEntity.ok(a.get(0));
//    }

    @Query("select p from Product p where p.name = :title")
    Product findByTitle(String title);
}
