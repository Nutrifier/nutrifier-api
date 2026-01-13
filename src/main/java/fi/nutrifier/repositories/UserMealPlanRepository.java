package fi.nutrifier.repositories;

import fi.nutrifier.entities.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserMealPlanRepository extends JpaRepository<MealPlan, String> {
    Optional<List<MealPlan>> findByUserId(String userId);
}