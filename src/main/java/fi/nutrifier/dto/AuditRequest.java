package fi.nutrifier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

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
}