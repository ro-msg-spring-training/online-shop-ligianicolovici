package ro.msg.learning.shop.repositories;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Stock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LocationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Integer> findLocationWithProductAndQuantity(Map<Integer, Integer> productWithQuantity) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> query = criteriaBuilder.createQuery(Integer.class);
        Root<Stock> stock = query.from(Stock.class);
        Path<Integer> productID = stock.get("product").get("id");
        Path<Integer> quantity = stock.get("quantity");
        Path<Integer> locationID = stock.get("location").get("id");
        List<Predicate> predicates = new ArrayList<>();

        productWithQuantity.forEach((prodID, prodQuantity) ->
                predicates.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(productID, prodID),
                                criteriaBuilder.greaterThanOrEqualTo(quantity, prodQuantity)
                        )
                ));

        query.select(locationID)
                .where(criteriaBuilder.or(predicates.toArray(new Predicate[0])))
                .groupBy(locationID)
                .having(criteriaBuilder.equal(criteriaBuilder.count(productID), productWithQuantity.size()));

        return entityManager.createQuery(query)
                .getResultList();

    }
}
