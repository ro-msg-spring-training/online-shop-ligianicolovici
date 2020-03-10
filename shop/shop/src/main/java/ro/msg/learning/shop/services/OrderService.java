package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.configuration.StrategyConfiguration;
import ro.msg.learning.shop.dtos.OrderDTO;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.mappers.ProductMapper;
import ro.msg.learning.shop.repositories.*;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CustomerRepository customerRepository;
    private final StrategyConfiguration strategyConfiguration;
    private final LocationRepository locationRepository;


    public OrderDTO createOrder(OrderDTO orderDTO) {
        OrderDTO newOrder;
        LocalDateTime localDateTime = LocalDateTime.now();
        List<OrderDetail> result = registerOrderedProducts(orderDTO.getOrderedProducts(), false);
        List<StockDTO> strategyResult = strategyConfiguration.decideStrategy().implementStrategy(orderDetailMapper.orderDetailListToOrderDetailDTOList(result));
        Set<Location> locationsForShippingProducts = getShippingLocations(strategyResult);
        if (strategyResult.size() > 0) {
            System.out.println(strategyResult.toString());
            Order createNewOrder = Order.builder()
                    .customer(customerRepository.findById(orderDTO.getUserID()).get())
                    .addressCity(orderDTO.getAddressCity())
                    .addressCountry(orderDTO.getAddressCountry())
                    .addressStreet(orderDTO.getAddressStreet())
                    .orderDetails(registerOrderedProducts(orderDTO.getOrderedProducts(), true))
                    .shippedFrom(locationsForShippingProducts)
                    .createdAt(localDateTime)
                    .build();

            List<OrderDetail> crtOrderDetails = createNewOrder.getOrderDetails();
            for (OrderDetail orderDetail : crtOrderDetails) {
                orderDetail.setOrder(createNewOrder);
            }
            orderRepository.save(createNewOrder);
            newOrder = orderMapper.orderToOrderDto(createNewOrder);
            return newOrder;
        } else {
            throw new ProductsCantBeShipped("No location to take products from");
        }
    }

    public Set<Location> getShippingLocations(List<StockDTO> stocks) {
        Set<Location> resultLocationsForShipping = new HashSet<>();
        for (StockDTO crtStock : stocks) {
            if (!resultLocationsForShipping.contains(locationRepository.findById(crtStock.getLocation_id()).get())) {
                resultLocationsForShipping.add(locationRepository.findById(crtStock.getLocation_id()).get());
            }
        }
        return resultLocationsForShipping;

    }

    public List<OrderDetail> registerOrderedProducts(List<OrderDetailDTO> productDetails, boolean saveData) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail crtOrderDetail = null;
        Optional<ProductDTO> productCheck;
        for (OrderDetailDTO productInfo : productDetails) {
            productCheck = Optional.ofNullable(productMapper.productToProductDTO(productRepository.findById(productInfo.getProductId()).get()));
            if (productCheck.isPresent()) {
                if (saveData == true) {
                    crtOrderDetail = orderDetailsRepository.save(orderDetailMapper.orderDetailDTOToOrderDetail(productInfo));
                    orderDetails.add(crtOrderDetail);
                } else {

                }
                orderDetails.add(orderDetailMapper.orderDetailDTOToOrderDetail(productInfo));
            } else {
                productCheck = null;
            }
        }
        return orderDetails;
    }
}
