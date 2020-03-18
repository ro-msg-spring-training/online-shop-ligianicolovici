package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Stock;

import java.util.List;
import java.util.Optional;

@Component
public interface StockRepository extends JpaRepository<Stock, Integer> {
    public Optional<Stock> findByLocationIdAndProductId(Integer locationID, Integer productID);

    public List<Stock> findAllByLocationId(Integer locationID);

    public List<Stock> findAllByProductId(Integer productID);
}
