package fi.nutrifier.mappers;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.DailyNutritionSummary;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DailyNutritionSummaryMapperWithUpdate implements EntityMapper<
        DailyNutritionSummary,
        DailyNutritionSummaryResponse,
        DailyNutritionSummaryCreateRequest
> {

    @Override
    public DailyNutritionSummaryResponse toResponse(DailyNutritionSummary entity) {
        if (entity == null) return null;
        return new DailyNutritionSummaryResponse(
                entity.getId(),
                entity.getDate(),
                entity.getCaloriesTarget(),
                entity.getFatTarget(),
                entity.getCarbTarget(),
                entity.getProteinTarget(),
                entity.getCaloriesConsumed(),
                entity.getFatConsumed(),
                entity.getCarbsConsumed(),
                entity.getProteinConsumed()
        );
    }

    @Override
    public DailyNutritionSummary toEntity(UUID userId, DailyNutritionSummaryCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return new DailyNutritionSummary(
            UUID.randomUUID(),
            userId,
            request.getDate(),
            request.getCaloriesTarget(),
            request.getFatTarget(),
            request.getCarbsTarget(),
                request.getProteinTarget(),
                0.0,
                0.0,
                0.0,
                0.0,
                now,
                now
        );
    }

    public void updateEntityFromRequest(DailyNutritionSummaryUpdateRequest request, DailyNutritionSummary entity) {
        entity.setDate(request.getDate());
        entity.setCaloriesTarget(request.getCaloriesGoal());
        entity.setFatTarget(request.getFatGoal());
        entity.setCarbTarget(request.getCarbsGoal());
        entity.setProteinTarget(request.getProteinGoal());
        entity.setCaloriesConsumed(request.getCaloriesConsumed());
        entity.setFatConsumed(request.getFatConsumed());
        entity.setCarbsConsumed(request.getCarbsConsumed());
        entity.setProteinConsumed(request.getProteinConsumed());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
