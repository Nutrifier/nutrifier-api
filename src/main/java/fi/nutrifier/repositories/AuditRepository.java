package fi.nutrifier.repositories;

import fi.nutrifier.entities.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditRepository extends JpaRepository<AuditLog, UUID> {
    Page<AuditLog> findAuditLogsByUserId(UUID userId, Pageable pageable);
    Page<AuditLog> findAuditLogsByCategory(String category, Pageable pageable);
    Page<AuditLog> findAuditLogsByUserIdAndCategory(UUID userId, String category, Pageable pageable);
}