package fi.nutrifier.exceptions;

import java.util.UUID;

public class ReferenceDataNotFoundException extends RuntimeException {
    public ReferenceDataNotFoundException() {
        super("Reference data not found.");
    }

    public ReferenceDataNotFoundException(String referenceDataName, UUID id) {
        super("Reference data for " + referenceDataName + " is not found for ID: " + id);
    }
}