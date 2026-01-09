package fi.nutrifier.services;

import fi.nutrifier.entities.UserLog;
import fi.nutrifier.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LogService {

    private final LogRepository repository;

    @Autowired
    public LogService(LogRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<UserLog> create(UserLog entity) {
        try {
            UserLog data = repository.save(entity);
            return new ResponseEntity<>(data, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<UserLog>> getAll(Integer page, Integer size) {
        System.out.println("Getting all logs");
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<UserLog> data = repository.findAll(pageRequest);

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<UserLog> getById(String id) {
        try {
            UserLog data = repository.findById(id).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<UserLog> update(String id, UserLog entity) {
        try {
            UserLog existingEntity = repository.findById(id).orElse(null);

            if (existingEntity != null) {
                UserLog data = repository.save(entity);
                return new ResponseEntity<>(data, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<UserLog> delete(String id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<UserLog>> getLogsByDateAndUser(LocalDate date, String userId) {
        try {
            List<UserLog> userLogs = repository.findByDateAndUserId(date, userId);
            return new ResponseEntity<>(userLogs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<UserLog>> getLogsByUserId(String id) {
        try {
            List<UserLog> userLogs = repository.findByUserId(id);
            return new ResponseEntity<>(userLogs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}