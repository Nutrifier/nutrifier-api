package fi.nutrifier.exceptions;

public class FailedCryptionException extends Exception {
    public FailedCryptionException(String str) {
        super(str);
    }
}
