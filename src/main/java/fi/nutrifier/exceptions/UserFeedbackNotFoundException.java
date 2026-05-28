package fi.nutrifier.exceptions;

public class UserFeedbackNotFoundException extends RuntimeException {
    public UserFeedbackNotFoundException() {
        super("User feedback not found!");
    }

    public UserFeedbackNotFoundException(String message) {
        super(message);
    }
}