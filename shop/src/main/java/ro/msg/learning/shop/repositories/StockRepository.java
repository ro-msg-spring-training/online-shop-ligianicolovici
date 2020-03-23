package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;

import java.util.List;
import java.util.Optional;

@Component
public interface StockRepository extends JpaRepository<Stock, Integer> {
    public Optional<Stock> findByLocationIdAndProductId(Integer locationID, Integer productID);

    @Query("select s from Stock s where s.location.id= (:locationID) and s.product.id in(:productIDList)")
    public List<Stock> findAllByLocationIdAndProductIdIn(@Param("locationID")Integer locationID,@Param("productIDList")List<Integer> productIDList);

    public List<Stock>findAllByLocationId(Integer locationID);

    public List<Stock> findAllByProductId(Integer productID);

    @Query("select s.location from Stock s where s.product.id in(:productIDList) group by s.location.id having count(s)>1")
    public List<Location> findAllLocationsWithStocksByProductIdIn(@Param("productIDList") List<Integer> productIDList);
}
