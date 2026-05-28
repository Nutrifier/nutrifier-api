package fi.nutrifier.repositories;

import fi.nutrifier.entities.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MealRepository extends JpaRepository<Meal, UUID> {
    Optional<Meal> findByUserIdAndId(UUID userId, UUID id);
    List<Meal> findByUserId(UUID userId);
    void deleteByUserIdAndId(UUID userId, UUID id);
}