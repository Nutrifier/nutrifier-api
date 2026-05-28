package fi.nutrifier.repositories;

import fi.nutrifier.entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserSettingsRepository extends JpaRepository<Settings, UUID> {
    Optional<Settings> findByUserId(UUID userId);
}