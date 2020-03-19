package ro.msg.learning.shop.mappers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDTO;
import ro.msg.learning.shop.dtos.OrderDTOScheduler;
import ro.msg.learning.shop.entities.Order;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderSchedulerMapper {

    private final OrderDetailMapper orderDetailMapper;

    public OrderDTOScheduler orderToOrderDtoSCH(Order order) {
        return OrderDTOScheduler.builder()
                .orderID(order.getId())
                .locationSet(order.getShippedFrom())
                .orderedProducts(orderDetailMapper.orderDetailListToOrderDetailDTOList(order.getOrderDetails()))
                .addressCity(order.getAddressCity())
                .addressCountry(order.getAddressCountry())
                .addressStreet(order.getAddressStreet())
                .build();
    }
    public List<OrderDTOScheduler> orderListToOrderListDTOSCH(List<Order>orderList){
        List<OrderDTOScheduler>result= new ArrayList<>();
        for(Order order:orderList){
            result.add(orderToOrderDtoSCH(order));
        }
        return  result;
    }
}
