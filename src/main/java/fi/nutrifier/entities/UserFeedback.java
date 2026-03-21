package fi.nutrifier.entities;

import fi.nutrifier.enums.FeedbackStatus;
import fi.nutrifier.enums.FeedbackType;
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
@Table(name = "user_feedbacks")
@NoArgsConstructor
@AllArgsConstructor
public class UserFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id; // Generating id in the mapper

    @Column(columnDefinition = "CHAR(36)")
    private UUID userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FeedbackType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;

    private String decisionReasoning;

    @Column(columnDefinition = "CHAR(36)")
    private UUID reviewedBy;

    private LocalDateTime reviewedAt;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
