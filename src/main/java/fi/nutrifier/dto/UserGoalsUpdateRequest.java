package fi.nutrifier.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fi.nutrifier.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserGoalsUpdateRequest {
    private String reasoning;
    private Double targetWeight;
    private LocalDate targetDate;
    private LocalDate reachedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
