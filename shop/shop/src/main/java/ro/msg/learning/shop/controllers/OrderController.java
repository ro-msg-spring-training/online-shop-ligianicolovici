package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.LocationDTO;
import ro.msg.learning.shop.dtos.OrderDTO;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.services.OrderService;
import ro.msg.learning.shop.services.StockService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/order")
public class OrderController {

    private final OrderService orderService;
    private final StockService stockService;
    @PostMapping
    public OrderDTO createNewOrder(@RequestBody OrderDTO orderDTO){
        return orderService.createOrder(orderDTO);
    }


}
