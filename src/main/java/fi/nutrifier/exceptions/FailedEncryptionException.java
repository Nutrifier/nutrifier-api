package fi.nutrifier.exceptions;

public class FailedEncryptionException extends FailedCryptionException {
    public FailedEncryptionException (String str) {
        super(str);
    }
}
