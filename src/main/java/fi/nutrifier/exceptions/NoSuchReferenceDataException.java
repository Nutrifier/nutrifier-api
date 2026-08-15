package fi.nutrifier.exceptions;

public class NoSuchReferenceDataException extends RuntimeException {
    public NoSuchReferenceDataException() {
        super("No such reference data.");
    }

    public NoSuchReferenceDataException(String referenceDataName, String name) {
        super("There is no \"" + name + "\" for " + referenceDataName);
    }
}