package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.CategoryDTO;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.ProductCategory;
import ro.msg.learning.shop.exceptions.ProductNotFoundException;
import ro.msg.learning.shop.mappers.ProductMapper;
import ro.msg.learning.shop.repositories.CategoryRepository;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository productCategoryRepo;

    public ProductDTO createProduct(ProductDTO productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .weight(productDto.getWeight())
                .imageUrl(productDto.getImageUrl())
                .productCategory(checkCategoryExistence(productDto.getProductCategory()))
                .build();
        productRepository.save(product);
        return productMapper.productToProductDTO(product);
    }

    public ProductCategory checkCategoryExistence(CategoryDTO category) {
        Optional<ProductCategory> searchedCategory = productCategoryRepo.findByName(category.getName());
        ProductCategory crtProductCategory = null;
        if (searchedCategory.isPresent()) {
            crtProductCategory = searchedCategory.get();
        } else {
            crtProductCategory = new ProductCategory();
            crtProductCategory.setName(category.getName());
            crtProductCategory.setDescription(category.getDescription());
            productCategoryRepo.save(crtProductCategory);
        }
        return crtProductCategory;

    }

    public ProductDTO updateProduct(Integer id, ProductDTO updatedProduct) {
        ProductDTO resultedProduct = null;
        Optional<Product> productToUpdate = productRepository.findById(id);
        if (productToUpdate.isPresent()) {
            Product updated = productRepository.findById(id).get();
            updated.setName(updatedProduct.getName());
            updated.setPrice(updatedProduct.getPrice());
            updated.setDescription(updatedProduct.getDescription());
            updated.setWeight(updatedProduct.getWeight());
            updated.setImageUrl(updatedProduct.getImageUrl());
            updated.setProductCategory(checkCategoryExistence(updatedProduct.getProductCategory()));
            productRepository.save(updated);
            resultedProduct = productMapper.productToProductDTO(updated);
        }
        return resultedProduct;
    }

    public void deleteProductById(Integer id) {
        productRepository.deleteById(id);
    }

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> existingProducts = new ArrayList<>();
        try {
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                throw new ProductNotFoundException("No products were found");
            } else {
                for (Product p : products) {
                    existingProducts.add(productMapper.productToProductDTO(p));
                }
            }
        } catch (ProductNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return existingProducts;

    }

    public ProductDTO getProductById(Integer id) {
        Optional<Product> searchedProduct = productRepository.findById(id);
        if (searchedProduct.isPresent()) {
            return productMapper.productToProductDTO(searchedProduct.get());
        } else {
            throw new ProductNotFoundException("Product not found!");
        }
    }
}
