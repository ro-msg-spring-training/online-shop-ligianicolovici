package ro.msg.learning.shop.jdbc.mappers;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.ProductCategory;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductCategoryRowMapper implements RowMapper<ProductCategory> {
    @Override
    public ProductCategory mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductCategory productCategory = new ProductCategory();

            productCategory.setId(resultSet.getInt("id"));
            productCategory.setName(resultSet.getString("name"));
            productCategory.setDescription(resultSet.getString("description"));

        return productCategory;
    }
}
