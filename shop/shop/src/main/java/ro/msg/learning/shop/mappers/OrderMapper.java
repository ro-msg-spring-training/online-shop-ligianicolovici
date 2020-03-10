package ro.msg.learning.shop.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDTO;
import ro.msg.learning.shop.entities.Order;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderDetailMapper orderDetailMapper;


    public OrderDTO orderToOrderDto(Order order) {
        return OrderDTO.builder()
                .orderID(order.getId())
                .userID(order.getCustomer().getId())
                .addressCity(order.getAddressCity())
                .addressCountry(order.getAddressCountry())
                .addressStreet(order.getAddressStreet())
                .orderedProducts(orderDetailMapper.orderDetailListToOrderDetailDTOList(order.getOrderDetails()))
                .build();
    }

}
