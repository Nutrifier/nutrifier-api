package fi.nutrifier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AuditResponse {
    private UUID id;
    private UUID userId;
    private String action;
    private String category;
    private String source;
    private LocalDateTime datetime;
    private String oldValue;
    private String newValue;
    private String session;
}