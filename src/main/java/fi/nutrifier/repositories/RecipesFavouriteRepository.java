package fi.nutrifier.repositories;

import fi.nutrifier.entities.RecipeFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipesFavouriteRepository extends JpaRepository<RecipeFavourite, UUID> {
    Optional<RecipeFavourite> findByUserIdAndRecipeId(UUID userId, UUID recipeId);
    List<RecipeFavourite> findByUserId(UUID userId);
    void deleteByUserIdAndRecipeId(UUID userId, UUID recipeId);
}