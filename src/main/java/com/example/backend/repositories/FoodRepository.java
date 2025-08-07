package com.example.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entities.Food;

import java.util.List;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, UUID> {
    List<Food> findFoodsByNameContainingIgnoreCase(String name);
}