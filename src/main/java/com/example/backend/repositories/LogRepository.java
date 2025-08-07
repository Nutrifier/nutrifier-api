package com.example.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entities.Log;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LogRepository extends JpaRepository<Log, UUID> {
    List<Log> findByDateAndUserId(LocalDate date, UUID userId);
    List<Log> findByUserId(UUID id);
}