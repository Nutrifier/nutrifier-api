package fi.nutrifier.repositories;

import fi.nutrifier.entities.WeightEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WeightRepository extends JpaRepository<WeightEntry, UUID> {
    Page<WeightEntry> findByUserIdOrderByDateDesc(UUID userId, Pageable pageable);
}