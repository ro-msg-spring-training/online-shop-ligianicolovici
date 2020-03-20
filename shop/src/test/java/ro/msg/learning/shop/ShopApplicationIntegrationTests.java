package ro.msg.learning.shop;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ro.msg.learning.shop.configurations.StrategyConfiguration;
import ro.msg.learning.shop.dtos.OrderDTO;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.mappers.*;
import ro.msg.learning.shop.repositories.*;
import ro.msg.learning.shop.services.OrderService;
import ro.msg.learning.shop.services.StockService;
import ro.msg.learning.shop.strategies.GreedyStrategy;
import ro.msg.learning.shop.strategies.MostAbundantStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;
import ro.msg.learning.shop.utils.Address;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
@DataJpaTest
@RequiredArgsConstructor
public class ShopApplicationIntegrationTests {

    private final CategoryMapper categoryMapper = new CategoryMapper();
    private final ProductMapper productMapper = new ProductMapper(categoryMapper);
    protected SingleLocationStrategy singleLocationStrategy;
    protected MostAbundantStrategy mostAbundantStrategy;
    protected GreedyStrategy closestLocationStrategy;

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    StockRepository stockRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailsRepository orderDetailRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    private OrderDetailMapper orderDetailMapper;
    private OrderMapper orderMapper;
    private StrategyConfiguration strategyConfiguration;
    private StockService stockService;
    private OrderService orderService;
    private Address address;

    private final StockMapper stockMapper = new StockMapper(locationRepository, productRepository);

    @Before
    public void initialize() {

        singleLocationStrategy = new SingleLocationStrategy(stockRepository, locationRepository, stockService);
        mostAbundantStrategy = new MostAbundantStrategy(stockRepository, stockService);
        closestLocationStrategy = new GreedyStrategy(stockRepository, stockService, stockMapper, orderRepository);
        strategyConfiguration = new StrategyConfiguration(singleLocationStrategy, mostAbundantStrategy, closestLocationStrategy);


        orderDetailMapper = new OrderDetailMapper(productRepository);
        orderMapper = new OrderMapper(orderDetailMapper);

        stockService = new StockService(stockRepository, locationRepository, stockMapper);
        orderService = new OrderService(orderRepository, orderMapper, orderDetailsRepository, orderDetailMapper, productRepository, productMapper, customerRepository, strategyConfiguration, locationRepository);

        ReflectionTestUtils.setField(strategyConfiguration, "strategy", "single_location");
        ReflectionTestUtils.setField(singleLocationStrategy, "stockRepository", stockRepository);
        ReflectionTestUtils.setField(singleLocationStrategy, "locationRepository", locationRepository);
        ReflectionTestUtils.setField(singleLocationStrategy, "stockService", stockService);

        ReflectionTestUtils.setField(mostAbundantStrategy, "stockRepository", stockRepository);
        ReflectionTestUtils.setField(mostAbundantStrategy, "stockService", stockService);


        ReflectionTestUtils.setField(strategyConfiguration, "singleLocationStrategy", singleLocationStrategy);
        ReflectionTestUtils.setField(strategyConfiguration, "mostAbundantStrategy", mostAbundantStrategy);
        address.setCity("test");
        address.setCountry("test");
        address.setStreet("test");
    }

    @Test
    public void singleCreateSuccess() {

        List<OrderDetailDTO> orderDetailDTOS = Arrays.asList(
                new OrderDetailDTO(10, 2),
                new OrderDetailDTO(4, 2)
        );
        OrderDTO orderCreationDTO = OrderDTO.builder()
                .address(address)
                .userID(10)
                .orderedProducts(orderDetailDTOS)
                .build();

        OrderDTO createdOrder = orderService.createOrder(orderCreationDTO);

        Assert.assertNotNull(createdOrder);
        Assert.assertEquals(createdOrder.getOrderedProducts().get(0).getProductId(), Optional.of(10).get());

        List<OrderDetail> details = orderDetailRepository.findAllByOrderId(createdOrder.getOrderID());

        Assert.assertEquals(details.get(0).getQuantity(), Optional.of(2).get());
        Assert.assertEquals(details.get(0).getProduct().getId(), Optional.of(10).get());
        Assert.assertEquals(details.get(0).getOrder().getId(), Optional.of(11).get());

        Assert.assertEquals(details.get(1).getQuantity(), Optional.of(2).get());
        Assert.assertEquals(details.get(1).getProduct().getId(), Optional.of(4).get());
        Assert.assertEquals(details.get(1).getOrder().getId(), Optional.of(11).get());

    }

    @Test(expected = ProductsCantBeShipped.class)
    public void singleCreateFail() {

        List<OrderDetailDTO> orderDetailDTOS = Arrays.asList(
                new OrderDetailDTO(1, 2),
                new OrderDetailDTO(4, 2)
        );
        OrderDTO orderCreationDTO = OrderDTO.builder()
                .address(address)
                .userID(10)
                .orderedProducts(orderDetailDTOS)
                .build();

        orderService.createOrder(orderCreationDTO);
    }
}
