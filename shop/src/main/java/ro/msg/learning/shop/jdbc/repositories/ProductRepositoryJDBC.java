package ro.msg.learning.shop.jdbc.repositories;

import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import ro.msg.learning.shop.configurations.SpringJdbcConfig;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.jdbc.mappers.ProductRowMapper;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryJDBC {
    private final SpringJdbcConfig springJdbcConfig = new SpringJdbcConfig();
    private DataSource dataSource = springJdbcConfig.mysqlDataSource();

    private final ProductRowMapper productRowMapper;
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    private final TransactionTemplate transactionTemplate;

    private static final Logger LOGGER = Logger.getLogger(ProductRepositoryJDBC.class);

    public Product findById(Integer id) {
        String queryFindById = "SELECT * FROM product WHERE ID = ?";
        try {
            Object[] params = new Object[]{id};
            return jdbcTemplate.queryForObject(queryFindById, params, productRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void deleteById(Integer id) {
        String queryDelete = "DELETE FROM product WHERE id = ? ";
        Object[] params = new Object[]{id};
        int rows = jdbcTemplate.update(queryDelete, params);
        LOGGER.info(rows + " row(s) deleted from Product table.");
    }

    public List<Product> findAll() {
        String queryFindAll = "SELECT * FROM product";
        try {
            return jdbcTemplate.query(queryFindAll, productRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public void save(Product product) {
        String queryCreate = "";

        int rows = 0;
        if (product.getId() == null) {
            queryCreate = "INSERT INTO product ( id, image_url, description, name, price, weight, product_category_id) values(?,?,?,?,?,?,?) ";
            Object[] params = new Object[]{product.getId(), product.getImageUrl(), product.getDescription(), product.getName(), product.getPrice(), product.getWeight(), product.getProductCategory().getId()};
            rows = jdbcTemplate.update(queryCreate, params);
        } else {
            queryCreate = "UPDATE product SET  image_url = ?, description = ?, name = ?, price = ? , weight = ?, product_category_id = ? WHERE ID = ?";
            rows = jdbcTemplate.update(queryCreate, product.getImageUrl(), product.getDescription(), product.getName(), product.getPrice(), product.getWeight(), product.getProductCategory().getId(), product.getId());
        }

        LOGGER.info(rows + " row(s) added/updated in Product table.");
    }
}