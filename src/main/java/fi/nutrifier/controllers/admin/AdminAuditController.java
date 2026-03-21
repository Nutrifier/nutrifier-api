package fi.nutrifier.controllers.admin;

import fi.nutrifier.dto.AuditResponse;
import fi.nutrifier.dto.FoodRequest;
import fi.nutrifier.dto.FoodResponse;
import fi.nutrifier.services.AuditService;
import fi.nutrifier.services.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@Tag(name = "Audit (Admin)")
@RestController
@RequestMapping("/api/admin/audit")
public class AdminAuditController {

    private final AuditService service;

    public AdminAuditController(AuditService service) {
        this.service = service;
    }

    @Operation(summary = "Get all audit logs")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @GetMapping
    public ResponseEntity<Page<AuditResponse>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "null") String userId,
            @RequestParam(defaultValue = "null") String category
    ) {
        if (!Objects.equals(userId, "null") && !Objects.equals(category, "null")) {
            return service.getAllByUserIdAndCategory(UUID.fromString(userId), category, page, size);
        } else if (!Objects.equals(userId, "null")) {
            return service.getAllByUserId(UUID.fromString(userId), page, size);
        } else if (!Objects.equals(category, "null")) {
            return service.getAllByCategory(category, page, size);
        }
        return service.getAll(page, size);
    }
}