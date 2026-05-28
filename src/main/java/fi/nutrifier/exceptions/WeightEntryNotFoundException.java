package fi.nutrifier.exceptions;

public class WeightEntryNotFoundException extends RuntimeException {
    public WeightEntryNotFoundException() {
        super("Weight entry not found!");
    }

    public WeightEntryNotFoundException(String message) {
        super(message);
    }
}