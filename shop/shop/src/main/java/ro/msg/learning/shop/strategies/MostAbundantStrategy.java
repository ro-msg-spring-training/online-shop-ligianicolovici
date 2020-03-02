package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.mappers.StockMapper;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.services.StockService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@RequiredArgsConstructor
public class MostAbundantStrategy implements StrategyChoiceInterface {

    @Autowired
    private  StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockMapper stockMapper;

    @Override
    public List<StockDTO> implementStrategy(List<OrderDetailDTO> orderDetailDTOList) {

        List<StockDTO> resultedStocks = new ArrayList<>();

        for(OrderDetailDTO demandedProduct:orderDetailDTOList){
            List<Stock> stocksContainingDemandedProduct = stockRepository.findAllByProduct_Id(demandedProduct.getProductId());

            Stock mostAbundant = stocksContainingDemandedProduct.stream().max(Comparator.comparing(Stock::getQuantity)).get();
            if(demandedProduct.getQuantity() <= mostAbundant.getQuantity()){
                StockDTO targetStock= StockDTO.builder()
                        .quantity(demandedProduct.getQuantity())
                        .location_id(mostAbundant.getLocation().getId())
                        .product_id(demandedProduct.getProductId())
                        .build();
                resultedStocks.add(targetStock);
                stockService.updateStock(demandedProduct.getProductId(),mostAbundant.getLocation().getId(),mostAbundant,demandedProduct.getQuantity());
                if(resultedStocks.size()== orderDetailDTOList.size()){
                    return resultedStocks;
                }
            }
        }
        throw new ProductsCantBeShipped("Products not available at the moment!");
    }
}
