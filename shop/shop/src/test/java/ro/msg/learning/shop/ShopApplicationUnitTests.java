package ro.msg.learning.shop;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ro.msg.learning.shop.configuration.StrategyConfiguration;
import ro.msg.learning.shop.converter.CSVConversion;
import ro.msg.learning.shop.dtos.CategoryDTO;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.*;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.mappers.OrderMapper;
import ro.msg.learning.shop.mappers.ProductMapper;
import ro.msg.learning.shop.mappers.StockMapper;
import ro.msg.learning.shop.repositories.*;
import ro.msg.learning.shop.services.OrderService;
import ro.msg.learning.shop.services.StockService;
import ro.msg.learning.shop.strategies.MostAbundantStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class ShopApplicationUnitTests {

    private static final String CSV = "location_id,product_id,quantity\n13,6,31\n";
    private static final String mockStrategy = "single_location";
    public StockRepository stockRepository = mock(StockRepository.class);
    public LocationRepository locationRepository = mock(LocationRepository.class);
    public StockMapper stockMapper = mock(StockMapper.class);
    public OrderRepository orderRepository = mock(OrderRepository.class);
    public OrderMapper orderMapper = mock(OrderMapper.class);
    public OrderDetailsRepository orderDetailsRepository = mock(OrderDetailsRepository.class);
    public OrderDetailMapper orderDetailMapper = mock(OrderDetailMapper.class);
    public ProductRepository productRepository = mock(ProductRepository.class);
    public ProductMapper productMapper = mock(ProductMapper.class);
    public CustomerRepository customerRepository = mock(CustomerRepository.class);
    int mockLocationId;
    Optional<Location> mockLocationOptional;
    List<Location> mockAllLocations = new ArrayList<>();
    List<Stock> mockStockList;
    List<Stock> mockStrategyStockList;
    List<OrderDetail> mockOrderDetails;
    List<StockDTO> mockStockDtoList;
    List<OrderDetailDTO> mockOrderDetailDTOS;
    List<StockDTO> resultedStocks;
    List<Location> allMockLocations;
    Product mockProduct;
    ProductDTO mockProductDTO;
    CategoryDTO mockCategoryDTO;
    ProductCategory mockCategory;
    List<Product> mockProductList;
    Supplier mockSupplier;
    @InjectMocks
    private StockDTO stockDTO;
    private CSVConversion<StockDTO> csvConversion;
    private StockService stockService = new StockService(stockRepository, locationRepository, stockMapper);
    private StrategyConfiguration strategyConfiguration = new StrategyConfiguration();
    private OrderService orderService = new OrderService(orderRepository, orderMapper, orderDetailsRepository, orderDetailMapper, productRepository, productMapper, customerRepository, strategyConfiguration, locationRepository);
    private SingleLocationStrategy mockSingleLocationStrategy = new SingleLocationStrategy();
    private MostAbundantStrategy mockMostAbundantStrategy = new MostAbundantStrategy();

    @Before
    public void beforeEachTest() {

        ReflectionTestUtils.setField(strategyConfiguration, "strategy", mockStrategy);
        ReflectionTestUtils.setField(mockSingleLocationStrategy, "stockRepository", stockRepository);
        ReflectionTestUtils.setField(mockSingleLocationStrategy, "locationRepository", locationRepository);
        ReflectionTestUtils.setField(mockSingleLocationStrategy, "stockService", stockService);
        ReflectionTestUtils.setField(strategyConfiguration, "singleLocationStrategy", mockSingleLocationStrategy);
        ReflectionTestUtils.setField(strategyConfiguration, "mostAbundantStrategy", mockMostAbundantStrategy);

        csvConversion = new CSVConversion();
        mockLocationId = 10;
        mockStockList = Collections.emptyList();
        mockStrategyStockList = new ArrayList<>();
        mockAllLocations = Collections.emptyList();
        resultedStocks = Collections.emptyList();
        mockOrderDetails = Collections.emptyList();
        mockLocationOptional = Optional.of(Location.builder().id(mockLocationId).build());
        allMockLocations = new ArrayList<>();
        if (mockLocationOptional.isPresent()) {
            allMockLocations.add(mockLocationOptional.get());
        }


        mockStockDtoList = new ArrayList<>();
        mockStockDtoList.add(StockDTO.builder().product_id(4).quantity(2).location_id(mockLocationId).build());
        mockStockDtoList.add(StockDTO.builder().product_id(5).quantity(3).location_id(mockLocationId).build());
        mockProductList = new ArrayList<>();
        mockSupplier = new Supplier(1, "supplier", mockProductList);
        mockCategory = new ProductCategory(1, "category", "categoryDescription", mockProductList);
        mockCategoryDTO = new CategoryDTO("category", "categoryDescription");
        mockProduct = new Product(10, "test", "descriere", new BigDecimal("0.03"), 221.22, "something", mockSupplier, mockCategory, mockStockList, mockOrderDetails);
        mockProductDTO = new ProductDTO("test", "descriere", new BigDecimal("0.03"), 221.22, "something", mockCategoryDTO);
        mockProductList.add(mockProduct);
        mockOrderDetailDTOS = new ArrayList<>();
        mockOrderDetailDTOS.add(new OrderDetailDTO(10, 2));
        mockOrderDetailDTOS.add(new OrderDetailDTO(4, 2));
        mockStrategyStockList.add(Stock.builder().product(mockProduct).location(allMockLocations.get(0)).quantity(2).id(10).build());


    }

    @Test
    public void exportStocksToCsvSuccess() throws Exception {
        Mockito.when((locationRepository.findById(anyInt()))).thenReturn(mockLocationOptional);
        Mockito.when((stockRepository.findAllByLocation_Id(anyInt()))).thenReturn(mockStockList);
        Mockito.when((stockMapper.stockListToStockListDTO(anyList()))).thenReturn(mockStockDtoList);

        Assert.assertEquals(stockService.exportStocksToCSV(mockLocationId), mockStockDtoList);
    }

    @Test
    public void fromCsvTestSuccess() throws IOException {
        List<StockDTO> expectedStocks = new ArrayList<>();
        expectedStocks.add(new StockDTO(13, 6, 31));
        InputStream inputStream = new ByteArrayInputStream(CSV.getBytes());

        Assert.assertEquals(expectedStocks, csvConversion.fromCsv(StockDTO.class, inputStream));
    }

    @Test
    public void toCsvTestSuccess() throws IOException {
        List<StockDTO> existingStocks = new ArrayList<>();
        existingStocks.add(new StockDTO(13, 6, 31));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        csvConversion.toCsv(StockDTO.class, existingStocks, outputStream);

        Assert.assertEquals(CSV, new String(outputStream.toByteArray()));
    }

    @Test
    public void singleLocationTestSuccessful() {
        List<StockDTO> resultedStock;
        Mockito.when((productRepository.findById(anyInt()))).thenReturn(Optional.ofNullable(mockProduct));
        Mockito.when((productMapper.productToProductDTO(any(Product.class)))).thenReturn(mockProductDTO);
        Mockito.when((orderDetailMapper.orderDetailListToOrderDetailDTOList(anyList()))).thenReturn(mockOrderDetailDTOS);
        Mockito.when((locationRepository.findAll())).thenReturn(allMockLocations);
        Mockito.when((stockRepository.findAllByLocation_Id(anyInt()))).thenReturn(mockStrategyStockList);

        List<OrderDetailDTO> orderDetailDTOS = Arrays.asList(
                new OrderDetailDTO(10, 2)
        );

        resultedStock = strategyConfiguration.decideStrategy().implementStrategy(orderDetailDTOS);
        Assert.assertEquals(resultedStock.get(0).getLocation_id(), Optional.of(10).get());

    }

    @Test(expected = ProductsCantBeShipped.class)
    public void singleLocationTestFail() {
        List<StockDTO> resultedStock;
        Mockito.when((productRepository.findById(anyInt()))).thenReturn(Optional.ofNullable(mockProduct));
        Mockito.when((productMapper.productToProductDTO(any(Product.class)))).thenReturn(mockProductDTO);
        Mockito.when((orderDetailMapper.orderDetailListToOrderDetailDTOList(anyList()))).thenReturn(mockOrderDetailDTOS);
        Mockito.when((locationRepository.findAll())).thenReturn(allMockLocations);
        Mockito.when((stockRepository.findAllByLocation_Id(anyInt()))).thenReturn(mockStrategyStockList);

        List<OrderDetailDTO> orderDetailDTOS = Arrays.asList(
                new OrderDetailDTO(10, 2),
                new OrderDetailDTO(4, 2)
        );
        strategyConfiguration.decideStrategy().implementStrategy(orderDetailDTOS);
    }


}
