package ro.msg.learning.shop.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockMapper {
    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;
    public StockDTO stockToStockDTO(Stock stock){
        return StockDTO.builder()
                .quantity(stock.getQuantity())
                .location_id(stock.getLocation().getId())
                .product_id(stock.getProduct().getId())
                .build();
    }
    public Stock stockDTOToStock(StockDTO stockDTO){
        return Stock.builder()
                .quantity(stockDTO.getQuantity())
                .location(locationRepository.findById(stockDTO.getLocation_id()).get())
                .product(productRepository.findById(stockDTO.getProduct_id()).get())
                .build();
    }
    public List<StockDTO> stockListToStockListDTO(List<Stock>stocks){
        List<StockDTO> resultedStocksDTO = new ArrayList<>();
        for(Stock crtStock: stocks){
           resultedStocksDTO.add(stockToStockDTO(crtStock));
        }
        return resultedStocksDTO;
    }
}
