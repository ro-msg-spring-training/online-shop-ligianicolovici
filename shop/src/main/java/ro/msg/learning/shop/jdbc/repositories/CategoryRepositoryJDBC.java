package ro.msg.learning.shop.jdbc.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ro.msg.learning.shop.configurations.SpringJdbcConfig;
import ro.msg.learning.shop.entities.ProductCategory;
import ro.msg.learning.shop.jdbc.mappers.ProductCategoryRowMapper;

import javax.sql.DataSource;

@Repository
@Transactional
public class CategoryRepositoryJDBC {
    private final SpringJdbcConfig springJdbcConfig = new SpringJdbcConfig();
    private DataSource dataSource = springJdbcConfig.mysqlDataSource();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;


    public ProductCategory findByCategoryName(String name) {
        String queryFindByName = "SELECT * FROM product_category WHERE NAME = ?";
        try {
            Object[] obj = new Object[]{name};
            return jdbcTemplate.queryForObject(queryFindByName, obj, new ProductCategoryRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    public ProductCategory findByCategoryId(Integer id) {
        String queryFindByName = "SELECT * FROM product_category WHERE ID = ?";
        try {
            Object[] obj = new Object[]{id};
            return jdbcTemplate.queryForObject(queryFindByName, obj, new ProductCategoryRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    public ProductCategory save(ProductCategory productCategory) {
        String queryCreate = "INSERT INTO product_category (id, name, description) values(?,?,?) ";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] obj = new Object[]{productCategory.getId(), productCategory.getName(), productCategory.getDescription()};
        jdbcTemplate.update(queryCreate, obj);
        return findByCategoryName(productCategory.getName());
    }
}


