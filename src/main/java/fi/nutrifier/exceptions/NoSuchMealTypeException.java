package fi.nutrifier.exceptions;

public class NoSuchMealTypeException extends RuntimeException {
    public NoSuchMealTypeException() {
        super("There is no such meal type.");
    }

    public NoSuchMealTypeException(String mealTypeStr) {
        super("There is no such meal type: " + mealTypeStr);
    }
}