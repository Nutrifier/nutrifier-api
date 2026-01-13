package fi.nutrifier.repositories;

import fi.nutrifier.entities.WeightEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserWeightRepository extends JpaRepository<WeightEntry, String> {
    Optional<List<WeightEntry>> findByUserIdOrderByDateDesc(String userId);
}