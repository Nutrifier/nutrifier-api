package fi.nutrifier.entities;

import fi.nutrifier.enums.ReportStatus;
import fi.nutrifier.enums.ReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "recipe_reports")
@NoArgsConstructor
@AllArgsConstructor
public class RecipeReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id; // Generating id in the mapper

    @Column(columnDefinition = "CHAR(36)")
    private UUID recipeId;

    @Column(columnDefinition = "CHAR(36)")
    private UUID userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private String description;
    private String decisionReasoning;

    @Column(columnDefinition = "CHAR(36)")
    private UUID reviewedBy;

    private LocalDateTime reviewedAt;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
