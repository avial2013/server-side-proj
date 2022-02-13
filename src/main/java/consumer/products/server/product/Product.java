package consumer.products.server.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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


    public Product(){

    }

    public Product(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }
}
