package fi.nutrifier.repositories;

import fi.nutrifier.entities.Goals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GoalsRepository extends JpaRepository<Goals, UUID> {
    Optional<Goals> findByUserId(UUID userId);
}