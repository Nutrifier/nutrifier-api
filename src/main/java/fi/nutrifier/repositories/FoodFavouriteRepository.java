package fi.nutrifier.repositories;

import fi.nutrifier.entities.FoodFavourite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodFavouriteRepository extends JpaRepository<FoodFavourite, UUID> {
    Optional<FoodFavourite> findByUserIdAndFoodId(UUID userId, UUID foodId);
    List<FoodFavourite> findByUserId(UUID userId);
    void deleteByUserIdAndFoodId(UUID userId, UUID foodId);
}