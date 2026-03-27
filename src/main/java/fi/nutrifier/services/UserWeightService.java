package fi.nutrifier.services;

import fi.nutrifier.entities.User;
import fi.nutrifier.entities.WeightEntry;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.repositories.WeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserWeightService {

    private final UserRepository userRepository;
    private final WeightRepository weightRepository;

    @Autowired
    public UserWeightService(UserRepository userRepository, WeightRepository weightRepository) {
        this.userRepository = userRepository;
        this.weightRepository = weightRepository;
    }

    public ResponseEntity<Page<WeightEntry>> getByUserId(UUID userId, Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<WeightEntry> data = weightRepository.findByUserIdOrderByDateDesc(userId, pageRequest);

            return new ResponseEntity<>(data, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<WeightEntry> create(UUID userId, Double weight) {
        try {
            User user = userRepository.findById(userId).orElse(null);

            if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            WeightEntry entry = new WeightEntry();
            entry.setUserId(user.getId());
            entry.setWeight(weight);
            entry.setDate(LocalDateTime.now());

            WeightEntry savedEntry = weightRepository.save(entry);

            return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
