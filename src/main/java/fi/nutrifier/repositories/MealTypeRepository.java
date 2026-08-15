package fi.nutrifier.repositories;

import fi.nutrifier.entities.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MealTypeRepository extends JpaRepository<MealType, UUID> {
    Optional<MealType> findByNameIgnoreCase(String name);
}