package ro.msg.learning.shop.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.entities.Product;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryMapper categoryMapper;

    public ProductDTO productToProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .productCategory(categoryMapper.categoryToCategoryDTO(product.getProductCategory()))
                .weight(product.getWeight())
                .build();
    }

    public List<ProductDTO> productListToProductDTOList(List<Product> products) {
        List<ProductDTO> result = new ArrayList<>();
        for (Product product : products) {
            result.add(productToProductDTO(product));
        }
        return result;
    }

}
