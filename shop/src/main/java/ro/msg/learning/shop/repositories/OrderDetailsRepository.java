package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.OrderDetail;

import java.util.List;
import java.util.Optional;

@Component
public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Integer> {
    public List<OrderDetail> findAllByOrderId(Integer orderId);

    public Optional<OrderDetail> findByProductId(Integer productID);
}
