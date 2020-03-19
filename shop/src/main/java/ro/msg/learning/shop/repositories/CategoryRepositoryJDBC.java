package ro.msg.learning.shop.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.ProductCategory;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryJDBC {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;


        public ProductCategory findByCategoryName(String name) {
            SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
            return namedParameterJdbcTemplate.queryForObject(
                    "SELECT * FROM PRODUCT_CATEGORY WHERE NAME = :name", namedParameters, ProductCategory.class);
        }


//
//    public Optional<ProductCategory> findByCategoryName(String name) {
//        return namedParameterJdbcTemplate.queryForObject(
//                "select * from product_category where name = :name",
//                new MapSqlParameterSource("name", name),
//                (rs, rowNum) ->
//                        Optional.of(new ProductCategory(
//                                rs.getInt("id"),
//                                rs.getString("name"),
//                                rs.getString("description"),
//                                (List<Product>) rs.getArray("products")
//                        )));
//    }
//    public int save(ProductCategory productCategory) {
//        return jdbcTemplate.update(
//                "insert into product_category (id, name, description) values(?,?,?)",
//                productCategory.getId(), productCategory.getName(),productCategory.getDescription());
//    }
}
