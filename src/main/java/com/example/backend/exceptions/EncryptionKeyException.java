package com.example.backend.exceptions;

public class EncryptionKeyException extends Exception {
    public EncryptionKeyException() {
        super("Encryption key not found!");
    }
}