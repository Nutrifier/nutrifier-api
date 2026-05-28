package fi.nutrifier.services;

import fi.nutrifier.dto.AuditRequest;
import fi.nutrifier.dto.AuditResponse;
import fi.nutrifier.entities.AuditLog;
import fi.nutrifier.repositories.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuditService {

    private final AuditRepository repository;

    @Autowired
    public AuditService(AuditRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<String> create(AuditRequest request, UUID userId) {
        repository.save(request.toEntity(userId));
        return new ResponseEntity<>(HttpStatus.CREATED); // No need to return audit logs, just saving in the background
    }

    public ResponseEntity<Page<AuditResponse>> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<AuditResponse> dtoPage = repository.findAll(pageRequest).map(AuditLog::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    public ResponseEntity<Page<AuditResponse>> getAllByUserId(UUID userId, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<AuditResponse> dtoPage = repository.findAuditLogsByUserId(userId, pageRequest).map(AuditLog::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    public ResponseEntity<Page<AuditResponse>> getAllByCategory(String category, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<AuditResponse> dtoPage = repository
                .findAuditLogsByCategory(category, pageRequest)
                .map(AuditLog::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    public ResponseEntity<Page<AuditResponse>> getAllByUserIdAndCategory(UUID userId, String category, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<AuditResponse> dtoPage = repository
                .findAuditLogsByUserIdAndCategory(userId, category, pageRequest)
                .map(AuditLog::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }
}