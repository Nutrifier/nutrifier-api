package fi.nutrifier.exceptions;

public class MealNotFoundException extends RuntimeException {
    public MealNotFoundException() {
        super("Meal not found!");
    }

    public MealNotFoundException(String message) {
        super(message);
    }
}