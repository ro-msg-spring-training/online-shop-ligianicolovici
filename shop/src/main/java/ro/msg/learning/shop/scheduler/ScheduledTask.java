package ro.msg.learning.shop.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDTOScheduler;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Revenue;
import ro.msg.learning.shop.exceptions.LocationNotFoundException;
import ro.msg.learning.shop.exceptions.ProductNotFoundException;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.mappers.OrderSchedulerMapper;
import ro.msg.learning.shop.mappers.RevenueMapper;
import ro.msg.learning.shop.repositories.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ScheduledTask {
    private final OrderRepository orderRepository;
    private final OrderSchedulerMapper orderSchedulerMapper;
    private final LocationRepository locationRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final ProductRepository productRepository;
    private final RevenueRepository revenueRepository;
    private final RevenueMapper revenueMapper;

    @Scheduled(cron = "0 44 18 * * *")
    public void reportOnTodayRevenue() {
        LocalDate today = LocalDate.now();
        LocalDateTime from = today.atStartOfDay();
        LocalDateTime to = today.plusDays(1).atStartOfDay();
        List<OrderDTOScheduler> ordersOfToday = orderSchedulerMapper.orderListToOrderListDTOSCH(orderRepository.findAllByCreatedAtBetween(from, to));
        Map<Integer, List<Integer>> ordersAndLocations = getOrderAndAssociatedLocationsList(ordersOfToday);
        Map<Integer, BigDecimal> locationCosts = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : ordersAndLocations.entrySet()) {
            List<OrderDetailDTO> orderDetailDTOS = orderDetailMapper.orderDetailListToOrderDetailDTOList(orderDetailsRepository.findAllByOrderId(entry.getKey()));
            BigDecimal sum = BigDecimal.ZERO;
            for (OrderDetailDTO orderDetailDTO : orderDetailDTOS) {
                Optional<Product> orderProduct = productRepository.findById(orderDetailDTO.getProductId());
                if (orderProduct.isPresent()) {
                    BigDecimal totalCost = orderProduct.get().getPrice().multiply(new BigDecimal(orderDetailDTO.getQuantity()));
                    sum = sum.add(totalCost);
                } else {
                    throw new ProductNotFoundException("Product not found!");
                }
            }
            for (Integer locationID : entry.getValue())
                locationCosts.put(locationID, sum.divide(new BigDecimal(2), 0, RoundingMode.HALF_UP));
        }
        for (Map.Entry<Integer, BigDecimal> entry : locationCosts.entrySet()) {
            Optional<Location> location = locationRepository.findById(entry.getKey());
            if (location.isPresent()) {
                Revenue revenueToRegister = Revenue.builder()
                        .sum(entry.getValue())
                        .date(LocalDate.now())
                        .location(location.get())
                        .build();
                revenueRepository.save(revenueToRegister);
            } else {
                throw new LocationNotFoundException("Location is not registered!");
            }

        }
    }

    public Map<Integer, List<Integer>> getOrderAndAssociatedLocationsList(List<OrderDTOScheduler> ordersOfToday) {
        Map<Integer, List<Integer>> result = new HashMap<>();
        for (OrderDTOScheduler orderDTOSCH : ordersOfToday) {
            List<Integer> currentOrderLocations = new ArrayList<>();
            for (Location location : orderDTOSCH.getLocationSet()) {
                currentOrderLocations.add(location.getId());
            }
            result.put(orderDTOSCH.getOrderID(), currentOrderLocations);
        }
        return result;
    }
}
