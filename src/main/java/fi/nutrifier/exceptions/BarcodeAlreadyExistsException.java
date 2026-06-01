package fi.nutrifier.exceptions;

public class BarcodeAlreadyExistsException extends RuntimeException {
    public BarcodeAlreadyExistsException() {
        super("Barcode already exists!");
    }

    public BarcodeAlreadyExistsException(String message) {
        super(message);
    }
}