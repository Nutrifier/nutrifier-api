package fi.nutrifier.exceptions;

public class FailedDecryptionException extends FailedCryptionException {
    public FailedDecryptionException(String str) {
        super(str);
    }
}
