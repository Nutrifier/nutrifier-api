package fi.nutrifier.services;

import fi.nutrifier.dto.AuditRequest;
import fi.nutrifier.dto.AuditResponse;
import fi.nutrifier.entities.AuditLog;
import fi.nutrifier.mappers.AuditMapper;
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
    private final AuditMapper mapper;

    @Autowired
    public AuditService(AuditRepository repository, AuditMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ResponseEntity<String> create(AuditRequest request, UUID userId) {
        try {
            AuditLog saved = repository.save(mapper.toEntity(userId, request));
            return new ResponseEntity<>(HttpStatus.CREATED); // No need to return audit logs, just saving in the background
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<AuditResponse>> getAll(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<AuditLog> entityPage = repository.findAll(pageRequest);

            Page<AuditResponse> dtoPage = entityPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<AuditResponse>> getAllByUserId(UUID userId, Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<AuditLog> entityPage = repository.findAuditLogsByUserId(userId, pageRequest);

            Page<AuditResponse> dtoPage = entityPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<AuditResponse>> getAllByCategory(String category, Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<AuditLog> entityPage = repository.findAuditLogsByCategory(category, pageRequest);

            Page<AuditResponse> dtoPage = entityPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<AuditResponse>> getAllByUserIdAndCategory(UUID userId, String category, Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<AuditLog> entityPage = repository.findAuditLogsByUserIdAndCategory(userId, category, pageRequest);

            Page<AuditResponse> dtoPage = entityPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}