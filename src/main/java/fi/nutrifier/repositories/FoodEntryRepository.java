package fi.nutrifier.repositories;
import fi.nutrifier.entities.FoodEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, String> {
    List<FoodEntry> findByDateAndUserId(LocalDate date, String userId);
    Page<FoodEntry> findByUserId(String id, Pageable pageable);
    Optional<FoodEntry> findByIdAndUserId(String id, String userId);
    void deleteByIdAndUserId(String id, String userId);

}