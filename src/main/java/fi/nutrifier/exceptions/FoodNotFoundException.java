package fi.nutrifier.exceptions;

public class FoodNotFoundException extends RuntimeException {
    public FoodNotFoundException() {
        super("Food not found!");
    }

    public FoodNotFoundException(String message) {
        super(message);
    }
}