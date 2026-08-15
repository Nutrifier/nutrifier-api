package fi.nutrifier.repositories;

import fi.nutrifier.entities.FoodServing;
import fi.nutrifier.entities.FoodServingId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FoodServingRepository extends JpaRepository<FoodServing, FoodServingId> {
    List<FoodServing> findAllById_FoodId(UUID foodId);
}