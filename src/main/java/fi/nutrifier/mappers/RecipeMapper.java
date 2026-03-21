package fi.nutrifier.mappers;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.Recipe;
import fi.nutrifier.entities.RecipeReport;
import fi.nutrifier.enums.ReportStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RecipeMapper implements EntityMapper<Recipe, RecipeResponse, RecipeRequest> {

    @Override
    public RecipeResponse toResponse(Recipe recipe) {
        if (recipe == null) return null;
        return new RecipeResponse(
                recipe.getId(),
                recipe.getUserId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getServings(),
                recipe.getPreparationTime(),
                recipe.getIsPublic(),
                recipe.getIsForked(),
                recipe.getSteps(),
                recipe.getIngredientSections()
        );
    }

    @Override
    public Recipe toEntity(UUID userId, RecipeRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return new Recipe(
                UUID.randomUUID(),
                userId,
                request.getName(),
                request.getDescription(),
                request.getServings(),
                request.getPreparationTime(),
                request.getIsPublic(),
                request.getIsForked(),
                now,
                now,
                request.getSteps(),
                request.getIngredientSections()
        );
    }

    public void updateEntityFromRequest(RecipeRequest request, Recipe entity) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setServings(request.getServings());
        entity.setPreparationTime(request.getPreparationTime());
        entity.setIsPublic(request.getIsPublic());
        entity.setIsForked(request.getIsForked());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setSteps(request.getSteps());
        entity.setIngredientSections(request.getIngredientSections());
    }

    public RecipeReport reportCreateRequestToEntity(UUID recipeId, UUID userId, RecipeReportCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return new RecipeReport(
                UUID.randomUUID(),
                recipeId,
                userId,
                request.getType(),
                request.getReason(),
                ReportStatus.PENDING,
                request.getDescription(),
                null,
                null,
                null,
                now
        );
    }

    public void reportUpdateRequestToEntity(UUID userId, RecipeReportReviewRequest request, RecipeReport entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setDecisionReasoning(request.getDecisionReasoning());
        entity.setReviewedBy(userId);
        entity.setReviewedAt(now);
    }

    public RecipeReportResponse reportEntityToResponse(RecipeReport report) {
        return new RecipeReportResponse(
            report.getId(),
            report.getRecipeId(),
            report.getUserId(),
            report.getType(),
            report.getReason(),
            report.getStatus(),
            report.getDescription(),
            report.getDecisionReasoning(),
            report.getReviewedBy(),
            report.getReviewedAt()
        );
    }
}
