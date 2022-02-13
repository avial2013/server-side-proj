package consumer.products.server.product;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductController {
    private final ProductDAL database;
    private ProductFactory productFactory;

    public ProductController(ProductDAL accessDatabase, ProductFactory aFactory){
        this.database = accessDatabase;
        this.productFactory = aFactory;
    }

//    @GetMapping("/products")
//    List<Product> getAllProducts(){
//        return database.findAll();
//    }

//    @GetMapping("/products")
//    CollectionModel<EntityModel<Product>> getAllProducts(){
//        List<EntityModel<Product>> products = database.findAll()
//                .stream().map(product -> EntityModel.of(product,
//                        linkTo(methodOn(ProductController.class).singleProduct(product.getId()))
//                                .withSelfRel(),
//                        linkTo(methodOn(ProductController.class).getAllProducts())
//                                .withRel("All products")))
//                .collect(Collectors.toList());
//        return CollectionModel.of(products, linkTo(methodOn(ProductController.class)
//                .getAllProducts()).withSelfRel());
//
//    }

    @GetMapping("/products")
    CollectionModel<EntityModel<Product>> getAllProducts(){
        List<EntityModel<Product>> products = database.findAll().stream()
                .map(product -> productFactory.toModel(product)) //.map(productFactory::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(products,
                linkTo(methodOn(ProductController.class)).withSelfRel());
    }

    // #########################
    // 1.
//    @GetMapping("/products")
//    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts(){
//        List<EntityModel<Product>> products = database.findAll().stream()
//                .map(product -> productFactory.toModel(product))
//                .collect(Collectors.toList());
//        return ResponseEntity
//                .ok(CollectionModel.of(products));
//    }
    // #########################


//    @PostMapping("/products")
//    Product createProduct(@RequestBody Product aProduct){
//        return database.save(aProduct);
//    }

     /*
    ResponseEntity is a container for our response derived
    from Spring Model-View-Controller module. enables to create an object and also
    return a custom response
    status 201 - created. when creating
     */
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


//    @GetMapping("products/{id}")
//    Product getSingleProduct(@PathVariable Long id){
//        return database.findById(id)
//                .orElseThrow(()->new ProductNotFoundException(id));
//    }

//    @GetMapping("products/{id}")
//    EntityModel<Product> singleProduct(@PathVariable Long id) {
//        Product product = database.findById(id)
//                .orElseThrow(()->new ProductNotFoundException(id));
//        return EntityModel.of(product,
//                linkTo(methodOn(ProductController.class).singleProduct(id))
//                        .withSelfRel(),
//                linkTo(methodOn(ProductController.class).getAllProducts())
//                        .withRel("Back to products"));
//
//    }

    @GetMapping("products/{id}")
    EntityModel<Product> singleProduct(@PathVariable Long id){
        Product product = database.findById(id)
                .orElseThrow(()->new ProductNotFoundException(id));
        return productFactory.toModel(product);
    }
    
        // #########################
    // 2. in ProductNotFoundException I added a ResponseEntity with status 404.
//    @GetMapping("products/{id}")
//    ResponseEntity<EntityModel<Product>> singleProduct(@PathVariable Long id){
//        Product product = database.findById(id)
//                .orElseThrow(()->new ProductNotFoundException(id));
//        return ResponseEntity.ok(productFactory.toModel(product));
//    }
    // #########################


//    @PutMapping("/products/{id}")
//    Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct){
//        return database.findById(id).map(oldProduct->{
//            oldProduct.setName(updatedProduct.getName());
//            oldProduct.setCategory((updatedProduct.getCategory()));
//            oldProduct.setPrice(updatedProduct.getPrice());
//            return database.save(oldProduct);
//        }).orElseGet(()->{
//            updatedProduct.setId(id);
//            return database.save(updatedProduct);
//        });
//    }

    @DeleteMapping("/product/{id}")
    void deleteProduct(@PathVariable Long id){
        database.deleteById(id);
    }


}
