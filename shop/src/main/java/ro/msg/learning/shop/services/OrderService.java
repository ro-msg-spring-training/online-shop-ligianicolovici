package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.configurations.StrategyConfiguration;
import ro.msg.learning.shop.dtos.OrderDTO;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.*;
import ro.msg.learning.shop.exceptions.ClientIsNotRegistered;
import ro.msg.learning.shop.exceptions.ProductNotFoundException;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.mappers.ProductMapper;
import ro.msg.learning.shop.repositories.*;
import ro.msg.learning.shop.utils.LocationFormatMapQuest;

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
        LocationFormatMapQuest deliveryLocation = new LocationFormatMapQuest(null, orderDTO.getAddress().getCity(), orderDTO.getAddress().getCountry());
        List<StockDTO> strategyResult = strategyConfiguration.decideStrategy().implementStrategy(orderDetailMapper.orderDetailListToOrderDetailDTOList(result), deliveryLocation);
        Set<Location> locationsForShippingProducts = getShippingLocations(strategyResult);
        if (!strategyResult.isEmpty()) {
            Optional<Customer> client = customerRepository.findById(orderDTO.getUserID());
            if (client.isPresent()) {
                Order createNewOrder = Order.builder()
                        .customer(client.get())
                        .address(orderDTO.getAddress())
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
                throw new ClientIsNotRegistered("Client is not registered in the system!");
            }
        } else {
            throw new ProductsCantBeShipped("No location to take products from");
        }
    }

    public Set<Location> getShippingLocations(List<StockDTO> stocks) {
        Set<Location> resultLocationsForShipping = new HashSet<>();
        for (StockDTO crtStock : stocks) {
            Optional<Location> crtStockLocation = locationRepository.findById(crtStock.getLocationId());
            crtStockLocation.ifPresent(resultLocationsForShipping::add);
        }
        return resultLocationsForShipping;

    }

    public List<OrderDetail> registerOrderedProducts(List<OrderDetailDTO> productDetails, boolean saveData) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail crtOrderDetail;
        Optional<ProductDTO> productCheck;
        for (OrderDetailDTO productInfo : productDetails) {
            Optional<Product> orderedProduct = productRepository.findById(productInfo.getProductId());
            if (orderedProduct.isPresent()) {
                productCheck = Optional.ofNullable(productMapper.productToProductDTO(orderedProduct.get()));
                if (productCheck.isPresent()) {
                    if (saveData) {
                        crtOrderDetail = orderDetailMapper.orderDetailDTOToOrderDetail(productInfo);
                        crtOrderDetail.setProduct(orderedProduct.get());
                        orderDetailsRepository.save(crtOrderDetail);
                        orderDetails.add(crtOrderDetail);
                    } else {
                        crtOrderDetail = orderDetailMapper.orderDetailDTOToOrderDetail(productInfo);
                        crtOrderDetail.setProduct(orderedProduct.get());
                        orderDetails.add(crtOrderDetail);
                    }
                }
            } else {
                throw new ProductNotFoundException("Product not found!");
            }
        }
        return orderDetails;
    }
}
