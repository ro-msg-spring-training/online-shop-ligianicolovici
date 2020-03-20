package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.services.StockService;
import ro.msg.learning.shop.utils.LocationFormatMapQuest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MostAbundantStrategy implements StrategyChoiceInterface {

    private final StockRepository stockRepository;
    private final StockService stockService;

    @Override
    public List<StockDTO> implementStrategy(List<OrderDetailDTO> orderDetailDTOList, LocationFormatMapQuest deliveryAddress) {

        List<StockDTO> resultedStocks = new ArrayList<>();

        for (OrderDetailDTO demandedProduct : orderDetailDTOList) {
            List<Stock> stocksContainingDemandedProduct = stockRepository.findAllByProductId(demandedProduct.getProductId());

            Stock mostAbundant = stocksContainingDemandedProduct.stream().max(Comparator.comparing(Stock::getQuantity)).get();
            if (demandedProduct.getQuantity() <= mostAbundant.getQuantity()) {
                StockDTO targetStock = StockDTO.builder()
                        .quantity(demandedProduct.getQuantity())
                        .locationId(mostAbundant.getLocation().getId())
                        .productId(demandedProduct.getProductId())
                        .build();
                resultedStocks.add(targetStock);
                stockService.updateStock(mostAbundant, demandedProduct.getQuantity());
                if (resultedStocks.size() == orderDetailDTOList.size()) {
                    return resultedStocks;
                }
            }
        }
        throw new ProductsCantBeShipped("Products not available at the moment!");
    }
}
