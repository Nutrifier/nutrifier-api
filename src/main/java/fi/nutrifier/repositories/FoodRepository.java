package fi.nutrifier.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import fi.nutrifier.entities.Food;

import java.util.List;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, UUID> {
    Page<Food> findFoodsByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Food> findFoodsByBarcodeContainingIgnoreCase(String barcode);
}