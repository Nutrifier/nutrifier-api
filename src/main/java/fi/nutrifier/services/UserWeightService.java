package fi.nutrifier.services;

import fi.nutrifier.dto.CreateWeighInRequest;
import fi.nutrifier.entities.User;
import fi.nutrifier.entities.WeightEntry;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.repositories.UserWeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserWeightService {

    private final UserRepository userRepository;
    private final UserWeightRepository userWeightRepository;

    @Autowired
    public UserWeightService(UserRepository userRepository, UserWeightRepository userWeightRepository) {
        this.userRepository = userRepository;
        this.userWeightRepository = userWeightRepository;
    }

    public ResponseEntity<List<WeightEntry>> getAllByUserId(String userId) {
        try {
            List<WeightEntry> entries = userWeightRepository.findByUserIdOrderByDateDesc(userId).orElse(null);

            if (entries == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(entries, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<WeightEntry>> create(String userId, CreateWeighInRequest request) {
        try {
            User user = userRepository.findById(userId).orElse(null);

            if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            WeightEntry entry = new WeightEntry();
            entry.setUser(user);
            entry.setWeight(request.getWeight());
            entry.setDate(LocalDateTime.now());

            List<WeightEntry> weightEntries = userWeightRepository.findByUserIdOrderByDateDesc(userId).orElse(null);

            if (weightEntries == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            WeightEntry weightEntry = userWeightRepository.save(entry);
            weightEntries.add(weightEntry);
            user.setWeightEntries(weightEntries);

            return new ResponseEntity<>(weightEntries, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
