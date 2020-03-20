package ro.msg.learning.shop.jdbc.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.jdbc.repositories.CategoryRepositoryJDBC;
import ro.msg.learning.shop.repositories.SupplierRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class ProductRowMapper implements RowMapper<Product> {
    private final CategoryRepositoryJDBC categoryRepositoryJDBC;
    private final SupplierRepository supplierRepository;

    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        Product product = new Product();

        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setWeight(resultSet.getDouble("weight"));
        product.setProductCategory(categoryRepositoryJDBC.findByCategoryId(resultSet.getInt("product_category_id")));
        product.setSupplier(supplierRepository.findById(resultSet.getInt("supplier_id")).isPresent() ? supplierRepository.findById(resultSet.getInt("supplier_id")).get() : null);

        return product;
    }
}

