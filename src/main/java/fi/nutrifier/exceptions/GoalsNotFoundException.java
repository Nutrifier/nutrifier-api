package fi.nutrifier.exceptions;

public class GoalsNotFoundException extends RuntimeException {
    public GoalsNotFoundException() {
        super("Goals not found!");
    }

    public GoalsNotFoundException(String message) {
        super(message);
    }
}