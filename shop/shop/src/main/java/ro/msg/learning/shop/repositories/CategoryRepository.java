package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.entities.ProductCategory;

import java.util.Optional;
@Component
public interface CategoryRepository extends JpaRepository<ProductCategory, Integer> {
    public Optional<ProductCategory> findByName(String name);

}
