package ro.msg.learning.shop.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ro.msg.learning.shop.dtos.OrderDTO;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.services.CartService;
import ro.msg.learning.shop.services.CustomerService;
import ro.msg.learning.shop.services.EmailService;
import ro.msg.learning.shop.services.OrderService;
import ro.msg.learning.shop.utils.Address;
import ro.msg.learning.shop.utils.Cart;
import ro.msg.learning.shop.utils.CartProduct;
import ro.msg.learning.shop.utils.OrderContent;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final EmailService emailService;

    @PostMapping(value = "/addToCart")
    public String registerOrder(@ModelAttribute(name = "demands") Cart orderedProducts, Model cartModel) {
        List<ProductDTO> orderProducts = cartService.addProductsInCart(orderedProducts);
        List<CartProduct> orderCartProducts = cartService.getCartProducts(orderedProducts);

        OrderContent order = new OrderContent();
        for (CartProduct orderCartProduct : orderCartProducts) {
            order.addCartProduct(orderCartProduct);
        }

        cartModel.addAttribute("orderItems", orderProducts);
        cartModel.addAttribute("orderCartProducts", orderCartProducts);
        cartModel.addAttribute("order", order);
        return "cart";
    }

    @PostMapping(value = "/registerProducts")
    public String placeOrder(@ModelAttribute(name = "order") OrderContent order, Model model) {
        cartService.buildOrder(order);
        Address address = new Address();

        model.addAttribute("address", address);
        return "address";
    }

    @PostMapping(value = "/orderDone")
    public String buildNewOrder(@ModelAttribute(name = "address") Address address) {
        String mailAddressReceiver = "test-9914e1@inbox.mailtrap.io";

        OrderDTO order = cartService.getOrderToBePlace();
        OrderContent orderContent = cartService.getOrderContent();

        order.setAddress(address);
        order = orderService.createOrder(order);

        emailService.sendMail(mailAddressReceiver, orderContent, order, EmailService.PLAIN_TEXT_EMAIL);
        emailService.sendMail(mailAddressReceiver, orderContent, order, EmailService.HTML_BODY_EMAIL);
        return "orderSuccess";
    }
}
