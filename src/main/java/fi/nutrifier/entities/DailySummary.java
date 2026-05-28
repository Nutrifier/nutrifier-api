package fi.nutrifier.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import fi.nutrifier.dto.DailySummaryResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

@Entity
@Data
@Table(name = "daily_summaries")
@NoArgsConstructor
@AllArgsConstructor
public class DailySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id; // Id formed inside mapper

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private UUID userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @Column(nullable = false) private double calorieTarget;
    @Column(nullable = false) private double fatTarget;
    @Column(nullable = false) private double carbTarget;
    @Column(nullable = false) private double proteinTarget;

    @Column(nullable = false) private Boolean confirmed;

    public DailySummaryResponse toResponse() {
        return new DailySummaryResponse(
                this.calorieTarget,
                this.fatTarget,
                this.carbTarget,
                this.proteinTarget,
                this.confirmed,
                0.0,
                0.0,
                0.0,
                0.0
        );
    }
}