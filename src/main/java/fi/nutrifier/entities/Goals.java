package fi.nutrifier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fi.nutrifier.enums.GoalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_goals")
@NoArgsConstructor
@AllArgsConstructor
public class Goals {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    @Column(nullable = false)
    private LocalDate startDate;

    private Double targetWeight;
    private LocalDate targetDate;

    @Column(nullable = false)
    private LocalDate startWeight;

    private LocalDate reachedDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "goals", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalPeriods> periods = new ArrayList<>();
}