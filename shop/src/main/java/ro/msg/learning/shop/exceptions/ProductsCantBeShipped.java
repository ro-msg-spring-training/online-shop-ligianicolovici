package ro.msg.learning.shop.exceptions;

public class ProductsCantBeShipped extends RuntimeException {
    public ProductsCantBeShipped(String message) {
        super(message);
    }
}
