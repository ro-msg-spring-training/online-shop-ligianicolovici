package ro.msg.learning.shop.exceptions;

public class ClientIsNotRegistered extends RuntimeException {
    public ClientIsNotRegistered(String message) {
        super(message);
    }
}
