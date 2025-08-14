package fi.nutrifier.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import fi.nutrifier.entities.Log;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LogRepository extends JpaRepository<Log, String> {
    List<Log> findByDateAndUserId(LocalDate date, String userId);
    List<Log> findByUserId(String id);
}