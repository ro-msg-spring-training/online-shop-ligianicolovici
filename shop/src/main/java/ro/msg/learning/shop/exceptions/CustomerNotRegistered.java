package ro.msg.learning.shop.exceptions;

public class CustomerNotRegistered extends RuntimeException {
    public CustomerNotRegistered(String message) {
        super(message);
    }
}
