package fi.nutrifier.dto;

import fi.nutrifier.enums.FeedbackStatus;
import fi.nutrifier.enums.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserFeedbackResponse {
    private UUID id;
    private UUID userId;
    private FeedbackType type;
    private String title;
    private String message;
    private FeedbackStatus status;
    private String decisionReasoning;
    private UUID reviewedBy;
    private LocalDateTime reviewedAt;
}