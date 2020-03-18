package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.services.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping(produces = {"application/json"})
    @ResponseBody
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable Integer id) {
        productService.deleteProductById(id);
    }

    @GetMapping(produces = {"application/json"})
    @ResponseBody
    public List<ProductDTO> readProducts() {
        return productService.getAllProducts();
    }

    @PostMapping(value = "/{id}", produces = {"application/json"})
    @ResponseBody
    public ProductDTO updateProduct(@PathVariable Integer id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    @GetMapping( value = "/{id}", produces = {"application/json"})
    @ResponseBody
    public ProductDTO readProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }
}
