package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.mappers.ProductMapper;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.services.CartService;
import ro.msg.learning.shop.services.CustomerService;
import ro.msg.learning.shop.utils.Cart;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final CustomerService customerService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @GetMapping(value = "/login")
    public String login(@ModelAttribute(name = "user") Customer user) {
        return "login";
    }

    @GetMapping(value = "/logout")
    public String logout(@ModelAttribute(name = "user") Customer user) {
        return "login";
    }


    @PostMapping(value = "/login")
    public String renderHomePage(@ModelAttribute(name = "user") Customer user, Model model) {
        List<ProductDTO> products = productMapper.productListToProductDTOList(productRepository.findAll());
        Cart demands = new Cart();
        model.addAttribute("products", products);
        model.addAttribute("demands", demands);
        return "home";
    }

    @GetMapping(value = "/home")
    public String returnToHomePage(Model model) {
        cartService.clearCart();
        List<ProductDTO> products = productMapper.productListToProductDTOList(productRepository.findAll());
        Cart demands = new Cart();

        Customer user = new Customer();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            user.setUsername(((User) principal).getUsername());
        }

        model.addAttribute("user", user);
        model.addAttribute("products", products);
        model.addAttribute("demands", demands);
        return "home";
    }

}
