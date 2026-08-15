package fi.nutrifier.repositories.reference.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface ReferenceDataRepository<T> extends JpaRepository<T, UUID> {
    Optional<T> findByNameIgnoreCase(String name);
    Optional<UUID> findIdByNameIgnoreCase(String name);
}