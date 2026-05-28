package fi.nutrifier.exceptions;

public class SettingsNotFoundException extends RuntimeException {
    public SettingsNotFoundException() {
        super("Settings not found!");
    }

    public SettingsNotFoundException(String message) {
        super(message);
    }
}