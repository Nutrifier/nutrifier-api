package fi.nutrifier.mappers;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.UserFeedback;
import fi.nutrifier.enums.FeedbackStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UserFeedbackMapper implements EntityMapper<UserFeedback, UserFeedbackResponse, UserFeedbackCreateRequest> {

    @Override
    public UserFeedback toEntity(UUID userId, UserFeedbackCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return new UserFeedback(
                UUID.randomUUID(),
                userId,
                request.getType(),
                request.getTitle(),
                request.getMessage(),
                FeedbackStatus.PENDING,
                null,
                null,
                null,
                now
        );
    }

    @Override
    public UserFeedbackResponse toResponse(UserFeedback entity) {
        if (entity == null) return null;
        return new UserFeedbackResponse(
                entity.getId(),
                entity.getUserId(),
                entity.getType(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getStatus(),
                entity.getDecisionReasoning(),
                entity.getReviewedBy(),
                entity.getReviewedAt()
        );
    }

    public void updateRequestToEntity(UUID userId, UserFeedbackReviewRequest request, UserFeedback entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setType(request.getType());
        entity.setTitle(request.getTitle());
        entity.setMessage(request.getMessage());
        entity.setStatus(request.getStatus());
        entity.setDecisionReasoning(request.getDecisionReasoning());
        entity.setReviewedBy(userId);
        entity.setReviewedAt(now);
    }
}
