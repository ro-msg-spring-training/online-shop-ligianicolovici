package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.CategoryDTO;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.ProductCategory;
import ro.msg.learning.shop.exceptions.ProductNotFoundException;
import ro.msg.learning.shop.jdbc.repositories.CategoryRepositoryJDBC;
import ro.msg.learning.shop.jdbc.repositories.ProductRepositoryJDBC;
import ro.msg.learning.shop.mappers.ProductMapper;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepositoryJDBC categoryRepositoryJDBC;
    private final ProductRepositoryJDBC productRepositoryJDBC;
    private static final Logger LOGGER = Logger.getLogger(ProductService.class);

    public ProductDTO createProduct(ProductDTO productDto) {
        ProductCategory givenProductCategory = checkCategoryExistence(productDto.getProductCategory());
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .weight(productDto.getWeight())
                .imageUrl(productDto.getImageUrl())
                .productCategory(givenProductCategory)
                .build();
        productRepositoryJDBC.save(product);
        return productMapper.productToProductDTO(product);
    }

    public ProductCategory checkCategoryExistence(CategoryDTO category) {
        Optional<ProductCategory> searchedCategory = Optional.ofNullable(categoryRepositoryJDBC.findByCategoryName(category.getName()));
        ProductCategory crtProductCategory = new ProductCategory();
        if (searchedCategory.isPresent()) {
            crtProductCategory = searchedCategory.get();
        } else {
            crtProductCategory.setName(category.getName());
            crtProductCategory.setDescription(category.getDescription());
            crtProductCategory = categoryRepositoryJDBC.save(crtProductCategory);
        }
        return crtProductCategory;

    }

    public ProductDTO updateProduct(Integer id, ProductDTO updatedProduct) {
        ProductDTO resultedProduct = null;
        Optional<Product> productToUpdate = Optional.ofNullable(productRepositoryJDBC.findById(id));
        if (productToUpdate.isPresent()) {
            Product updated = productToUpdate.get();
            updated.setName(updatedProduct.getName());
            updated.setPrice(updatedProduct.getPrice());
            updated.setDescription(updatedProduct.getDescription());
            updated.setWeight(updatedProduct.getWeight());
            updated.setImageUrl(updatedProduct.getImageUrl());
            updated.setProductCategory(checkCategoryExistence(updatedProduct.getProductCategory()));
            productRepositoryJDBC.save(updated);
            resultedProduct = productMapper.productToProductDTO(updated);
        }
        return resultedProduct;
    }

    public void deleteProductById(Integer id) {
        productRepositoryJDBC.deleteById(id);
    }

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> existingProducts = new ArrayList<>();
        try {
            List<Product> products = productRepositoryJDBC.findAll();
            if (products.isEmpty()) {
                throw new ProductNotFoundException("No products were found");
            } else {
                for (Product product : products) {
                    existingProducts.add(productMapper.productToProductDTO(product));
                }
            }
        } catch (ProductNotFoundException ex) {
            LOGGER.info(ex.getMessage());
        }
        return existingProducts;

    }

    public ProductDTO getProductById(Integer id) {
        Optional<Product> searchedProduct = Optional.ofNullable(productRepositoryJDBC.findById(id));
        if (searchedProduct.isPresent()) {
            return productMapper.productToProductDTO(searchedProduct.get());
        } else {
            throw new ProductNotFoundException("Product not found!");
        }
    }
}
