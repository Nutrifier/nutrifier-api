package com.example.backend.exceptions;

public class FailedEncryptionException extends FailedCryptionException {
    public FailedEncryptionException (String str) {
        super(str);
    }
}
