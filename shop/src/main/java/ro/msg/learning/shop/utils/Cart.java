package ro.msg.learning.shop.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Cart {
    private List<Integer> productsIDs;
}
