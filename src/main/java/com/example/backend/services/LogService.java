package com.example.backend.services;

import com.example.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.backend.entities.Log;
import com.example.backend.repositories.LogRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class LogService {

    private final LogRepository repository;

    @Autowired
    public LogService(LogRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<Log> create(Log entity) {
        try {
            Log data = repository.save(entity);
            return new ResponseEntity<>(data, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<Log>> getAll(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Log> data = repository.findAll(pageRequest);

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Log> getById(UUID id) {
        try {
            Log data = repository.findById(id).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Log> update(UUID id, Log entity) {
        try {
            Log existingEntity = repository.findById(id).orElse(null);

            if (existingEntity != null) {
                Log data = repository.save(entity);
                return new ResponseEntity<>(data, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Log> delete(UUID id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Log>> getLogsByDateAndUser(LocalDate date, UUID userId) {
        try {
            List<Log> logs = repository.findByDateAndUserId(date, userId);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Log>> getLogsByUserId(UUID id) {
        try {
            List<Log> logs = repository.findByUserId(id);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}