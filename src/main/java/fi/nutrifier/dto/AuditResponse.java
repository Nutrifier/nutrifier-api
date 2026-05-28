package fi.nutrifier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class AuditResponse extends ApiResponse {
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