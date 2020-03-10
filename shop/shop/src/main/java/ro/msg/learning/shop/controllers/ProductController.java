package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.services.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;

    @RequestMapping(method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public void deleteProduct(@PathVariable Integer id) {
        productService.deleteProductById(id);
    }

    @RequestMapping(method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public List<ProductDTO> readProducts() {
        return productService.getAllProducts();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}", produces = {"application/json"})
    @ResponseBody
    public ProductDTO updateProduct(@PathVariable Integer id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = {"application/json"})
    @ResponseBody
    public ProductDTO readProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }
}
