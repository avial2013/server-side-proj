package consumer.products.server.order;

import consumer.products.server.product.Product;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate purchaseDate;
    private String title;
    private Double price;

    @OneToMany
    @JoinColumn(name = "products", nullable = false)
    private List<Product> productList;

    public Order(LocalDate purchaseDate, String title, Double price, List<Product> productList) {
        this.purchaseDate = purchaseDate;
        this.title = title;
        this.price = price;
        this.productList = productList;
    }

    public Order() {
    }

}
