package fi.nutrifier.dto;

import fi.nutrifier.entities.MealEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class MealResponse extends ApiResponse {
    private UUID id;
    private UUID userId;
    private String name;
    private Boolean isPublic;
    private Boolean isForked;
    private List<MealEntry> entries;
}