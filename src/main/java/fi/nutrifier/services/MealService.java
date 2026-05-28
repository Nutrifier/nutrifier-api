package fi.nutrifier.services;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.*;
import fi.nutrifier.exceptions.MealNotFoundException;
import fi.nutrifier.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MealService {

    private final MealRepository repository;
    private final MealFavouriteRepository favouriteRepository;

    @Autowired
    public MealService(
            MealRepository repository,
            MealFavouriteRepository favouriteRepository
    ) {
        this.repository = repository;
        this.favouriteRepository = favouriteRepository;
    }

    public ResponseEntity<MealResponse> create(MealRequest request, UUID userId) {
        Meal saved = repository.save(request.toEntity(userId));
        return new ResponseEntity<>(saved.toResponse(), HttpStatus.CREATED);
    }

    public ResponseEntity<Page<MealResponse>> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<MealResponse> dtoPage = repository.findAll(pageRequest).map(Meal::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    public ResponseEntity<MealResponse> getById(UUID id) {
        Meal data = repository.findById(id).orElseThrow(MealNotFoundException::new);
        return new ResponseEntity<>(data.toResponse(), HttpStatus.OK);
    }

    // TODO: Let only owner update the meal, otherwise create a private copy
    public ResponseEntity<MealResponse> update(UUID id, UUID userId, MealRequest request) {
        Meal existing = repository.findById(id).orElseThrow(MealNotFoundException::new);

        existing.updateEntityFromRequest(request);
        Meal saved = repository.save(existing);

        return new ResponseEntity<>(saved.toResponse(), HttpStatus.OK);
    }

    public ResponseEntity<MealResponse> delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new MealNotFoundException();
        }

        repository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<String> markAsFavourite(UUID mealId, UUID userId) {
        MealFavourite favourite = new MealFavourite(userId, mealId, LocalDateTime.now());

        favouriteRepository.save(favourite);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<String> removeFavourite(UUID mealId, UUID userId) {
        favouriteRepository.deleteByUserIdAndMealId(userId, mealId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<List<MealResponse>> getAllFavourites(UUID userId) {
        List<UUID> favouriteIds = favouriteRepository.findByUserId(userId)
                .orElseThrow(() -> new MealNotFoundException("Favourite meal not found"))
                .stream().map(MealFavourite::getMealId).toList();

        List<MealResponse> mapped = repository.findAllById(favouriteIds).stream().map(Meal::toResponse).toList();

        return new ResponseEntity<>(mapped, HttpStatus.OK);
    }
}