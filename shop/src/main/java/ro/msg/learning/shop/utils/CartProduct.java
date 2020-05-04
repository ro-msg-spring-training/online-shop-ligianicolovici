package ro.msg.learning.shop.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {
    private String name;
    private Integer productID;
    private Integer quantity;

    @Override
    public String toString() {
        return name + " | " + quantity + "\n";
    }
}
