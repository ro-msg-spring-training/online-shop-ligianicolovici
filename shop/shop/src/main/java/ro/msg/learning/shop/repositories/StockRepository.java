package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.Stock;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock,Integer> {
    public Optional<Stock> findByLocation_IdAndProduct_Id(Integer locationID, Integer productID);
    public List<Stock> findAllByLocation_Id(Integer locationID);
    public List<Stock> findAllByProduct_Id(Integer productID);
}
