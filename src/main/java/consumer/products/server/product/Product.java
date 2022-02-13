package consumer.products.server.product;

import consumer.products.server.order.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
/*
 @Data is a lombok annotation that generates:
 Getters, Setters, equals, hashcode, toString
 */

@Entity
public class Product {
    private @Id @GeneratedValue Long id;
    private String name;
    //private Enum category;
    private String category;
    private double price;
    public String description;

    @ManyToOne
    private Order order;



    public Product(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }
    public Product(){

    }
}
