package fi.nutrifier.dto;

import fi.nutrifier.enums.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFeedbackCreateRequest {
    private FeedbackType type;
    private String title;
    private String message;
}