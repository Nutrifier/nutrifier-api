package fi.nutrifier.repositories;

import fi.nutrifier.entities.FoodServing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FoodServingRepository extends JpaRepository<FoodServing, UUID> {
    List<FoodServing> findAllByFoodId(UUID foodId);
}