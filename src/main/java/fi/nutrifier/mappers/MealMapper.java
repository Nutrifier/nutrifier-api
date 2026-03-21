package fi.nutrifier.mappers;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.Meal;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class MealMapper implements EntityMapper<Meal, MealResponse, MealRequest> {

    @Override
    public MealResponse toResponse(Meal meal) {
        if (meal == null) return null;
        return new MealResponse(
                meal.getId(),
                meal.getUserId(),
                meal.getName(),
                meal.getIsPublic(),
                meal.getIsForked(),
                meal.getEntries()
        );
    }

    @Override
    public Meal toEntity(UUID userId, MealRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return new Meal(
                UUID.randomUUID(),
                userId,
                request.getName(),
                request.getIsPublic(),
                request.getIsForked(),
                now,
                now,
                request.getEntries()
        );
    }

    public void updateEntityFromRequest(MealRequest request, Meal entity) {
        entity.setName(request.getName());
        entity.setIsPublic(request.getIsPublic());
        entity.setIsForked(request.getIsForked());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setEntries(request.getEntries());
    }
}
