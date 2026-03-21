package fi.nutrifier.mappers;

import fi.nutrifier.dto.AuditRequest;
import fi.nutrifier.dto.AuditResponse;
import fi.nutrifier.entities.AuditLog;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AuditMapper implements EntityMapper<AuditLog, AuditResponse, AuditRequest> {

    @Override
    public AuditLog toEntity(UUID userId, AuditRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return new AuditLog(
                UUID.randomUUID(),
                userId,
                request.getAction(),
                request.getCategory(),
                request.getSource(),
                request.getDatetime(),
                request.getOldValue(),
                request.getNewValue(),
                request.getSession(),
                now    // Created at
        );
    }

    @Override
    public AuditResponse toResponse(AuditLog log) {
        if (log == null) return null;
        return new AuditResponse(
                log.getId(),
                log.getUserId(),
                log.getAction(),
                log.getCategory(),
                log.getSource(),
                log.getDatetime(),
                log.getOldValue(),
                log.getNewValue(),
                log.getSession()
        );
    }
}
