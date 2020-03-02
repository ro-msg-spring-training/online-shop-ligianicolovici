package ro.msg.learning.shop.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDTO;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.repositories.OrderDetailsRepository;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final CustomerRepository customerRepository;
    public OrderDTO orderToOrderDto(Order order){
        return OrderDTO.builder()
                .userID(order.getCustomer().getId())
                .addressCity(order.getAddressCity())
                .addressCountry(order.getAddressCountry())
                .addressStreet(order.getAddressStreet())
                .orderedProducts(orderDetailMapper.orderDetailListToOrderDetailDTOList(order.getOrderDetails()))
                .build();
    }

}
