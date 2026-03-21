package fi.nutrifier.repositories;

import fi.nutrifier.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    Optional<Recipe> findByUserIdAndId(UUID userId, UUID id);
    List<Recipe> findByUserId(UUID userId);
    void deleteByUserIdAndId(UUID userId, UUID id);
}