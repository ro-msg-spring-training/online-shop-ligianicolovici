package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.dtos.CategoryDTO;
import ro.msg.learning.shop.entities.ProductCategory;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<ProductCategory,Integer> {
    public Optional<ProductCategory> findByName(String name);

}
