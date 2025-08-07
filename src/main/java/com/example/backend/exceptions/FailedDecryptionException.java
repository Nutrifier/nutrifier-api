package com.example.backend.exceptions;

public class FailedDecryptionException extends FailedCryptionException {
    public FailedDecryptionException(String str) {
        super(str);
    }
}
