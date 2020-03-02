package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.OrderDetail;

import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail,Integer>{
    public Optional<OrderDetail> findAllByOrder_Id(Integer orderId);
    public Optional<OrderDetail> findByProduct_Id(Integer productID);
}
