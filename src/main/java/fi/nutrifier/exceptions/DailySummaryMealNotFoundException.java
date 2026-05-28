package fi.nutrifier.exceptions;

public class DailySummaryMealNotFoundException extends RuntimeException {
    public DailySummaryMealNotFoundException() {
        super("Daily summary meal not found!");
    }

    public DailySummaryMealNotFoundException(String message) {
        super(message);
    }
}