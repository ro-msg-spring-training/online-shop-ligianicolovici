package ro.msg.learning.shop.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDTO;
import ro.msg.learning.shop.entities.Order;

import java.util.ArrayList;
import java.util.List;

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
    public OrderDTO orderToOrderDtoSCH(Order order) {
        return OrderDTO.builder()
                .orderID(order.getId())
                .userID(order.getCustomer().getId())
                .addressCity(order.getAddressCity())
                .addressCountry(order.getAddressCountry())
                .addressStreet(order.getAddressStreet())
                .build();
    }
    public List<OrderDTO> orderListToOrderListDTO(List<Order>orderList){
        List<OrderDTO>result= new ArrayList<>();
        for(Order order:orderList){
            result.add(orderToOrderDtoSCH(order));
        }
        return  result;
    }

}
