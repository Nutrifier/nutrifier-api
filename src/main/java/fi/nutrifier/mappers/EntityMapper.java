package fi.nutrifier.mappers;

import java.util.UUID;

interface EntityMapper<E, R, Q> {
    E toEntity(UUID userId, Q request);
    R toResponse(E entity);
}
