package fi.nutrifier.services;

import fi.nutrifier.entities.Food;
import fi.nutrifier.entities.Log;
import fi.nutrifier.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class FoodService {

    private final FoodRepository repository;

    @Autowired
    public FoodService(FoodRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<Food> create(Food entity) {
        try {
            Food data = repository.save(entity);
            return new ResponseEntity<>(data, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Exception in adding food:" + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<Food>> getAll(Integer page, Integer size) {
        try {
            System.out.println("Getting all foods");
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Food> data = repository.findAll(pageRequest);

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Food> getById(String id) {
        try {
            Food data = repository.findById(id).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Food> update(String id, Food entity) {
        try {
            Food existingEntity = repository.findById(id).orElse(null);

            if (existingEntity != null) {
                Food data = repository.save(entity);
                return new ResponseEntity<>(data, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Food> delete(String id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Food>> getFoodsByQuery(String query) {
        try {
            List<Food> data = repository.findFoodsByNameContainingIgnoreCase(query);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Food>> getFoodsByBarcode(String query) {
        try {
            List<Food> data = repository.findFoodsByBarcodeContainingIgnoreCase(query);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}