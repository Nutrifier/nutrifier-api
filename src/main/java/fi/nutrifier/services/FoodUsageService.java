package fi.nutrifier.services;

import fi.nutrifier.entities.*;
import fi.nutrifier.repositories.FoodUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FoodUsageService {

    private final FoodUsageRepository repository;

    @Autowired
    public FoodUsageService(FoodUsageRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<String> track(UUID userId, FoodEntry entry) {
        FoodUsage usage = repository.findByUserIdAndFoodId(userId, entry.getFoodId()).orElse(null);
        LocalDateTime now = LocalDateTime.now();
        if (usage != null) {
            usage.setUsageCount(usage.getUsageCount() + 1);
            usage.setLastUsedAt(now);
            repository.save(usage);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // TODO: Add functionality to save Fineli foods usage
            if (entry.getFineliId() == null) {
                FoodUsage newUsage = new FoodUsage(userId, entry.getFoodId(), 1, now);
                repository.save(newUsage);

                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}