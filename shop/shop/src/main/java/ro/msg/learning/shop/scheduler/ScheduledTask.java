package ro.msg.learning.shop.scheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDTOScheduler;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Revenue;
import ro.msg.learning.shop.mappers.OrderDetailMapper;
import ro.msg.learning.shop.mappers.OrderSchedulerMapper;
import ro.msg.learning.shop.mappers.RevenueMapper;
import ro.msg.learning.shop.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);


    @Scheduled(cron = "0 0 19 * * *")
    public void reportOnTodayRevenue() {
        LocalDate today = LocalDate.now();
        LocalDateTime from = today.atStartOfDay();
        LocalDateTime to = today.plusDays(1).atStartOfDay();

        List<OrderDTOScheduler> ordersOfToday = orderSchedulerMapper.orderListToOrderListDTOSCH(orderRepository.findAllByCreatedAtBetween(from, to));
        Map<Integer, List<Integer>> ordersAndLocations = new HashMap<>();
        for (OrderDTOScheduler orderDTOSCH : ordersOfToday) {
            List<Integer> currentOrderLocations = new ArrayList<>();
            for (Location location : orderDTOSCH.getLocationSet()) {
                currentOrderLocations.add(location.getId());
            }
            ordersAndLocations.put(orderDTOSCH.getOrderID(), currentOrderLocations);
        }
        Map<Integer, BigDecimal> locationCosts = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : ordersAndLocations.entrySet()) {
            List<OrderDetailDTO> orderDetailDTOS = orderDetailMapper.orderDetailListToOrderDetailDTOList(orderDetailsRepository.findAllByOrder_Id(entry.getKey()));
            BigDecimal sum = BigDecimal.ZERO;
            for (OrderDetailDTO orderDetailDTO : orderDetailDTOS) {
                BigDecimal totalCost = productRepository.findById(orderDetailDTO.getProductId()).get().getPrice().multiply(new BigDecimal(orderDetailDTO.getQuantity()));
                sum = sum.add(totalCost);
            }
            for (Integer locationID : entry.getValue()) {
                locationCosts.put(locationID, sum.divide(new BigDecimal(2)));
            }

        }
        for (Map.Entry<Integer, BigDecimal> entry : locationCosts.entrySet()) {
            Revenue revenueToRegister = Revenue.builder()
                    .sum(entry.getValue())
                    .date(LocalDate.now())
                    .location(locationRepository.findById(entry.getKey()).get())
                    .build();
            Revenue saved = revenueRepository.save(revenueToRegister);
            log.info(revenueMapper.revenueToRevenueDTO(saved).toString());
        }
    }
}
