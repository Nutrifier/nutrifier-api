package fi.nutrifier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_goals")
@NoArgsConstructor
@AllArgsConstructor
public class UserGoals {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String id;

    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String user_id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "reasoning", length = 20)
    private String reasoning;

    @Column(name = "target_weight")
    private Double targetWeight;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "reached_date")
    private LocalDate reachedDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void initialize(String reasoning, Double targetWeight, LocalDate targetDate) {
        this.setReasoning(reasoning);
        this.setTargetWeight(targetWeight);
        this.setTargetDate(targetDate);
    }
}