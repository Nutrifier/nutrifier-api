package fi.nutrifier.dto;

import fi.nutrifier.entities.AuditLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AuditRequest {
    private String action;
    private String category;
    private String source;
    private LocalDateTime datetime;
    private String oldValue;
    private String newValue;
    private String session;

    public AuditLog toEntity(UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        return new AuditLog(
                UUID.randomUUID(),
                userId,
                this.getAction(),
                this.getCategory(),
                this.getSource(),
                this.getDatetime(),
                this.getOldValue(),
                this.getNewValue(),
                this.getSession(),
                now    // Created at
        );
    }
}