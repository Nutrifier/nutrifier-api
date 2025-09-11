package fi.nutrifier.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import fi.nutrifier.entities.Food;

import java.util.List;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, String> {
    List<Food> findFoodsByNameContainingIgnoreCase(String name);
    List<Food> findFoodsByBarcodeContainingIgnoreCase(String barcode);
}