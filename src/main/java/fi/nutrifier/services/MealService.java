package fi.nutrifier.services;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.*;
import fi.nutrifier.mappers.MealMapper;
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
    private final MealMapper mapper;

    @Autowired
    public MealService(
            MealRepository repository,
            MealFavouriteRepository favouriteRepository,
            MealMapper mapper
    ) {
        this.repository = repository;
        this.favouriteRepository = favouriteRepository;
        this.mapper = mapper;
    }

    public ResponseEntity<MealResponse> create(MealRequest request, UUID userId) {
        try {
            Meal saved = repository.save(mapper.toEntity(userId, request));
            return new ResponseEntity<>(mapper.toResponse(saved), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<MealResponse>> getAll(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Meal> foodPage = repository.findAll(pageRequest);

            Page<MealResponse> dtoPage = foodPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<MealResponse> getById(UUID id) {
        try {
            Meal data = repository.findById(id).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(mapper.toResponse(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TODO: Let only owner update the meal, otherwise create a private copy
    public ResponseEntity<MealResponse> update(UUID id, UUID userId, MealRequest request) {
        try {
            Meal existing = repository.findById(id).orElse(null);

            if (existing != null) {
                mapper.updateEntityFromRequest(request, existing);

                Meal saved = repository.save(existing);
                return new ResponseEntity<>(mapper.toResponse(saved), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<MealResponse> delete(UUID id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> markAsFavourite(UUID mealId, UUID userId) {
        try {
            MealFavourite favourite = new MealFavourite(userId, mealId, LocalDateTime.now());
            favouriteRepository.save(favourite);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> removeFavourite(UUID mealId, UUID userId) {
        try {
            favouriteRepository.deleteByUserIdAndMealId(userId, mealId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<MealResponse>> getAllFavourites(UUID userId) {
        try {
            List<MealFavourite> favourites = favouriteRepository.findByUserId(userId);
            List<Meal> foods = repository.findAllById(favourites.stream().map(MealFavourite::getMealId).toList());
            List<MealResponse> mapped = foods.stream().map(mapper::toResponse).toList();

            return new ResponseEntity<>(mapped, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}