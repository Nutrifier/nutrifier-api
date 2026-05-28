package fi.nutrifier.repositories;

import fi.nutrifier.entities.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DailySummaryRepository extends JpaRepository<DailySummary, UUID> {
    DailySummary findByDateAndUserId(LocalDate date, UUID userId);
}