package fi.nutrifier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fi.nutrifier.dto.AuditRequest;
import fi.nutrifier.dto.AuditResponse;
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
@Table(name = "audit_logs")
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID userId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(nullable = false, length = 20)
    private String source;

    @Column(nullable = false)
    private LocalDateTime datetime;

    private String oldValue;
    private String newValue;
    private String session;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public AuditResponse toResponse() {
        return new AuditResponse(
                this.id,
                this.userId,
                this.action,
                this.category,
                this.source,
                this.datetime,
                this.oldValue,
                this.newValue,
                this.session
        );
    }

    public AuditRequest toRequest() {
        return new AuditRequest(
                this.action,
                this.category,
                this.source,
                this.datetime,
                this.oldValue,
                this.newValue,
                this.session
        );
    }
}
