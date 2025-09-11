package fi.nutrifier.controllers;

import fi.nutrifier.services.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fi.nutrifier.entities.Log;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Logs Controller")
@RestController
@RequestMapping("/api/logs")
public class LogController {

    protected final LogService service;

    public LogController(LogService service) {
        this.service = service;
    }

    @Operation(summary = "Create a log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<Log> create(@Valid @RequestBody Log entity) {
        return service.create(entity);
    }

    @Operation(summary = "Update log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping("/{id}")
    public ResponseEntity<Log> update(@PathVariable("id") String id, @Valid @RequestBody Log item) {
        return service.update(id, item);
    }

    @Operation(summary = "Delete log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @DeleteMapping("/{id}")
    public ResponseEntity<Log> delete(@PathVariable("id") String id) {
        return service.delete(id);
    }

    @Operation(summary = "Get logs by date and user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/by-date")
    public ResponseEntity<List<Log>> getLogsByDateAndUser(@RequestParam String date, @RequestParam String userId) {
        LocalDate parsedDate = LocalDate.parse(date);
        return service.getLogsByDateAndUser(parsedDate, userId);
    }
}