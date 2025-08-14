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
@Table(name = "logs")
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String id;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

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
    private String meal;

    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private String userId;

    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private String foodId;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    private Double amount;
}
