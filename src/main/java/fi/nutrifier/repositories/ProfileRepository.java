package fi.nutrifier.repositories;

import fi.nutrifier.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByUserId(UUID userId);
}