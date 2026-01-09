package fi.nutrifier.repositories;
import fi.nutrifier.entities.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LogRepository extends JpaRepository<UserLog, String> {
    List<UserLog> findByDateAndUserId(LocalDate date, String userId);
    List<UserLog> findByUserId(String id);
}