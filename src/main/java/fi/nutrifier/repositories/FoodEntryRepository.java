package fi.nutrifier.repositories;
import fi.nutrifier.entities.FoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, String> {
    List<FoodEntry> findByDateAndUserId(LocalDate date, String userId);
    List<FoodEntry> findByUserId(String id);
}