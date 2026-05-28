package fi.nutrifier.exceptions;

public class GoalsPeriodFoundException extends RuntimeException {
    public GoalsPeriodFoundException() {
        super("Goal period not found!");
    }

    public GoalsPeriodFoundException(String message) {
        super(message);
    }
}