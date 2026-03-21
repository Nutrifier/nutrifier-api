package fi.nutrifier.repositories;

import fi.nutrifier.entities.FoodUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodUsageRepository extends JpaRepository<FoodUsage, UUID> {
    Optional<FoodUsage> findByUserIdAndFoodId(UUID userId, UUID foodId);
    List<FoodUsage> findTop5ByUserIdAndLastUsedAtAfterOrderByLastUsedAtDesc(UUID userId, LocalDateTime date);
}