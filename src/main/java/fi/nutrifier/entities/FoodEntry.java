package fi.nutrifier.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@Table(name = "food_entries")
@NoArgsConstructor
@AllArgsConstructor
public class FoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id; // Id formed inside mapper

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(nullable = false)
    @NotNull
    private LocalTime time;

    @Column(nullable = false)
    @NotNull
    private String mealType;

    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private UUID userId;

    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private UUID foodId;

    private Integer fineliId;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    private Double amount;
}
