package fi.nutrifier.services.reference.base;

import fi.nutrifier.exceptions.NoSuchReferenceDataException;
import fi.nutrifier.repositories.reference.base.ReferenceDataRepository;

import java.util.UUID;

public abstract class ReferenceDataService<T> {

    private final ReferenceDataRepository<T> repository;

    protected String entityName;

    protected ReferenceDataService(ReferenceDataRepository<T> repository, String entityName) {
        this.repository = repository;
        this.entityName = entityName;
    }

    public T of(String str) {
        if (str == null) {
            return null;
        }

        return repository.findByNameIgnoreCase(str)
                .orElseThrow(() -> new NoSuchReferenceDataException(entityName, str));
    }

    public UUID idOf(String str) {
        if (str == null) {
            return null;
        }

        return repository.findIdByNameIgnoreCase(str)
                .orElseThrow(() -> new NoSuchReferenceDataException(entityName, str));
    }
}