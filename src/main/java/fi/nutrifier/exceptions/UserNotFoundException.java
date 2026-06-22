package fi.nutrifier.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found!");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(UUID userId) {
        super("User not found with ID: " + userId);
    }
}