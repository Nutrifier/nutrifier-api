package fi.nutrifier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FoodEntryResponse {
    private UUID id;
    private LocalDate date;
    private LocalTime time;
    private String mealType;
    private UUID userId;
    private UUID foodId;
    private Integer fineliId;
    private Double amount;
}