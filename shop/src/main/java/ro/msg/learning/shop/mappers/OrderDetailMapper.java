package ro.msg.learning.shop.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.exceptions.ProductNotFoundException;
import ro.msg.learning.shop.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderDetailMapper {
    private final ProductRepository productRepository;

    public OrderDetailDTO orderDetailToOrderDetailDTO(OrderDetail orderDetail) {
        return OrderDetailDTO.builder()
                .productId(orderDetail.getProduct().getId())
                .quantity(orderDetail.getQuantity())
                .build();
    }

    public OrderDetail orderDetailDTOToOrderDetail(OrderDetailDTO orderDetailDTO) {
        Optional<Product>orderProduct= productRepository.findById(orderDetailDTO.getProductId());
        if(orderProduct.isPresent()){
            return OrderDetail.builder()
                    .product(orderProduct.get())
                    .quantity(orderDetailDTO.getQuantity())
                    .build();
        }else{
            throw new ProductNotFoundException("Product not found!");
        }

    }

    public List<OrderDetailDTO> orderDetailListToOrderDetailDTOList(List<OrderDetail> orderDetailList) {
        List<OrderDetailDTO> orderDetailDTO = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetailDTO.add(orderDetailToOrderDetailDTO(orderDetail));
        }
        return orderDetailDTO;
    }

    public List<OrderDetail> orderDetailDTOListToOrderDetailList(List<OrderDetailDTO> orderDetailListDTO) {
        List<OrderDetail> orderDetail = new ArrayList<>();
        for (OrderDetailDTO crtOrderDetailDTO : orderDetailListDTO) {
            orderDetail.add(orderDetailDTOToOrderDetail(crtOrderDetailDTO));
        }
        return orderDetail;
    }
}
