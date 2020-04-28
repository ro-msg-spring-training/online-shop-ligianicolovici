package ro.msg.learning.shop.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.OrderDTO;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.mappers.ProductMapper;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.utils.*;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class CartService {
    private OrderDTO orderToBePlace;
    private OrderContent orderContent;

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CustomerService customerService;

    public List<ProductDTO> addProductsInCart(Cart cart) {
        List<ProductDTO> demandedProducts = new ArrayList<>();
        for (Integer productId : cart.getProductsIDs()) {
            demandedProducts.add(productMapper.productToProductDTO(productRepository.findById(productId).get()));
        }
        return demandedProducts;
    }

    public List<CartProduct> getCartProducts(Cart cart) {
        List<CartProduct> demandedProducts = new ArrayList<>();
        for (Integer productId : cart.getProductsIDs()) {
            demandedProducts.add(new CartProduct(productRepository.findById(productId).get().getName(), productId, 1));
        }
        return demandedProducts;
    }

    public void buildOrder(OrderContent order) {
        orderContent = order;
        orderToBePlace = new OrderDTO();
        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        Address deliveryAddress = new Address();
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomerPrinciple) {
            username = ((CustomerPrinciple) principal).getUsername();
        } else {
            username = principal.toString();
        }
        orderToBePlace.setUserID(customerService.getCustomerIdByUsername(username));
        for (CartProduct orderProductInfo : order.getToOrderCartProducts()) {
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            orderDetailDTO.setProductId(orderProductInfo.getProductID());
            orderDetailDTO.setQuantity(orderProductInfo.getQuantity());
            orderDetails.add(orderDetailDTO);
        }
        orderToBePlace.setOrderedProducts(orderDetails);
        orderToBePlace.setAddress(deliveryAddress);
    }

    public void clearCart() {
        orderToBePlace.setAddress(new Address());
        orderToBePlace.setOrderedProducts(new ArrayList<>());
        orderToBePlace.setUserID(null);
        orderToBePlace.setOrderID(null);
    }


}
