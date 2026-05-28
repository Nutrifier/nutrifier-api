package fi.nutrifier.dto;

import fi.nutrifier.entities.UserFeedback;
import fi.nutrifier.enums.FeedbackStatus;
import fi.nutrifier.enums.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserFeedbackCreateRequest {
    private FeedbackType type;
    private String title;
    private String message;

    public UserFeedback toEntity(UUID userId) {
        return new UserFeedback(
                UUID.randomUUID(),
                userId,
                this.getType(),
                this.getTitle(),
                this.getMessage(),
                FeedbackStatus.PENDING,
                null,
                null,
                null,
                LocalDateTime.now()
        );
    }
}