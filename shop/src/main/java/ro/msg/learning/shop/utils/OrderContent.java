package ro.msg.learning.shop.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderContent {
    private List<CartProduct> toOrderCartProducts = new ArrayList<>();

    public void addCartProduct(CartProduct product) {
        this.toOrderCartProducts.add(product);
    }
}
