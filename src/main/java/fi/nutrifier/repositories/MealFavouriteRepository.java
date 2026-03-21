package fi.nutrifier.repositories;

import fi.nutrifier.entities.MealFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MealFavouriteRepository extends JpaRepository<MealFavourite, UUID> {
    Optional<MealFavourite> findByUserIdAndMealId(UUID userId, UUID mealId);
    List<MealFavourite> findByUserId(UUID userId);
    void deleteByUserIdAndMealId(UUID userId, UUID mealId);
}