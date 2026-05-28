package fi.nutrifier.exceptions;

public class FoodEntryNotFoundException extends RuntimeException {
    public FoodEntryNotFoundException() {
        super("Food entry not found!");
    }

    public FoodEntryNotFoundException(String message) {
        super(message);
    }
}