package ro.msg.learning.shop.repositories;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.utils.LocationAndProductIDs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class StockRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Stock> findStocksByLocationAndProductIDs(List<LocationAndProductIDs> locationAndProductIDsList) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stock> query = cb.createQuery(Stock.class);
        Root<Stock> stock = query.from(Stock.class);
        Path<Integer> locationIdPath = stock.get("location").get("id");
        Path<Integer> productIdPath = stock.get("product").get("id");
        List<Predicate> predicates = new ArrayList<>();

        locationAndProductIDsList.forEach(locationProductPair ->
                predicates.add(
                        cb.and(
                                cb.equal(locationIdPath, locationProductPair.getLocationID()),
                                cb.equal(productIdPath, locationProductPair.getProductID())
                        )
                ));

        query.select(stock)
                .where(cb.or(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query)
                .getResultList();
    }
}
