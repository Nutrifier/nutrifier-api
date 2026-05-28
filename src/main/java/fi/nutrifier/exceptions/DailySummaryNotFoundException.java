package fi.nutrifier.exceptions;

public class DailySummaryNotFoundException extends RuntimeException {
    public DailySummaryNotFoundException() {
        super("Daily summary not found!");
    }

    public DailySummaryNotFoundException(String message) {
        super(message);
    }
}