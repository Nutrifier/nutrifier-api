package fi.nutrifier.repositories;

import fi.nutrifier.entities.DailyNutritionSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface DailyNutritionSummaryRepository extends JpaRepository<DailyNutritionSummary, UUID> {
    DailyNutritionSummary findByDateAndUserId(LocalDate date, UUID userId);
    Page<DailyNutritionSummary> findByUserId(UUID id, Pageable pageable);
    Optional<DailyNutritionSummary> findByIdAndUserId(UUID id, UUID userId);
}