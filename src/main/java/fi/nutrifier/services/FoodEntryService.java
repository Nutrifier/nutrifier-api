package fi.nutrifier.services;

import fi.nutrifier.entities.Food;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.repositories.FoodEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FoodEntryService {

    private final FoodEntryRepository repository;

    @Autowired
    public FoodEntryService(FoodEntryRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<FoodEntry> create(String userId, FoodEntry entity) {
        try {
            entity.setUserId(userId);
            FoodEntry data = repository.save(entity);
            return new ResponseEntity<>(data, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<FoodEntry>> getAll(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<FoodEntry> data = repository.findAll(pageRequest);

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<FoodEntry>> getAllByUserId(String userId, Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<FoodEntry> data = repository.findByUserId(userId, pageRequest);

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FoodEntry> getById(String id) {
        try {
            FoodEntry data = repository.findById(id).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FoodEntry> getByIdAndUserId(String id, String userId) {
        try {
            FoodEntry data = repository.findByIdAndUserId(id, userId).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FoodEntry> update(String userId, String id, FoodEntry entity) {
        try {
            FoodEntry existingEntity = repository.findByIdAndUserId(id, userId).orElse(null);

            if (existingEntity != null) {
                FoodEntry data = repository.save(entity);
                return new ResponseEntity<>(data, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FoodEntry> delete(String userId, String id) {
        try {
            repository.deleteByIdAndUserId(id, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<FoodEntry>> getLogsByDateAndUser(LocalDate date, String userId) {
        try {
            List<FoodEntry> foodEntries = repository.findByDateAndUserId(date, userId);
            return new ResponseEntity<>(foodEntries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}