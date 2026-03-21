package fi.nutrifier.repositories;
import fi.nutrifier.entities.FoodEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, UUID> {
    List<FoodEntry> findByDateAndUserId(LocalDate date, UUID userId);
    Page<FoodEntry> findByUserId(UUID id, Pageable pageable);
    Optional<FoodEntry> findByIdAndUserId(UUID id, UUID userId);
    void deleteByIdAndUserId(UUID id, UUID userId);

}