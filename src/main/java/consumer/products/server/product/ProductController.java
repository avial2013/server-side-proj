package consumer.products.server.product;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ProductController {
    private final ProductDAL database;
    private ProductFactory productFactory;

    public ProductController(ProductDAL accessDatabase, ProductFactory aFactory){
        this.database = accessDatabase;
        this.productFactory = aFactory;
    }


    
//     1.
   @GetMapping("/products")
   public ResponseEntity<?> getAllProducts(@RequestParam Optional<String> name){
       if(name.isPresent()) {
           return ResponseEntity.ok(database.findByTitle(name.get()));
       }

       List<EntityModel<Product>> products = database.findAll().stream()
               .map(product -> productFactory.toModel(product))
               .collect(Collectors.toList());
       return ResponseEntity
               .ok(CollectionModel.of(products));
   }


    @PostMapping("/products")
    ResponseEntity<?> createProduct(@RequestBody Product product){
        EntityModel<Product> entityProduct =
                productFactory.toModel(database.save(product));

        return ResponseEntity
                .created(entityProduct.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityProduct);
    }

    @PutMapping("/products/{id}")
    ResponseEntity<?> replaceProduct(@RequestBody Product aProduct, @PathVariable Long id){
        Product updatedProduct =  database.findById(id).map(productToUpdate->{
            productToUpdate.setName(aProduct.getName());
            productToUpdate.setCategory((aProduct.getCategory()));
            productToUpdate.setPrice(aProduct.getPrice());
            return database.save(productToUpdate);

        })
                .orElseGet(()->{
                    aProduct.setId(id);
                    return database.save(aProduct);
            });
        EntityModel<Product> entityProduct =
                productFactory.toModel(database.save(updatedProduct));
        return ResponseEntity
                .created(entityProduct.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityProduct);
    }



    
    // 2. 
   @GetMapping("products/{id}")
   public ResponseEntity<?> singleProduct(@PathVariable Long id){
       Product product = database.findById(id)
               .orElseThrow(()->new ProductNotFoundException(id));
       return ResponseEntity.ok(productFactory.toModel(product));
   }



    @DeleteMapping("/product/{id}")
    void deleteProduct(@PathVariable Long id){
        database.deleteById(id);
    }


}
