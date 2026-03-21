package fi.nutrifier.dto;

import fi.nutrifier.enums.FeedbackStatus;
import fi.nutrifier.enums.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFeedbackReviewRequest {
    private FeedbackType type;
    private String title;
    private String message;
    private FeedbackStatus status;
    private String decisionReasoning;
}