package fi.nutrifier.services;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.*;
import fi.nutrifier.exceptions.RecipeNotFoundException;
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
public class RecipeService {

    private final RecipeRepository repository;
    private final RecipesFavouriteRepository favouriteRepository;
    private final RecipeReportRepository reportRepository;

    @Autowired
    public RecipeService(
            RecipeRepository repository,
            RecipesFavouriteRepository favouriteRepository,
            RecipeReportRepository reportRepository
    ) {
        this.repository = repository;
        this.favouriteRepository = favouriteRepository;
        this.reportRepository = reportRepository;
    }

    public ResponseEntity<RecipeResponse> create(RecipeRequest request, UUID userId) {
        Recipe saved = repository.save(request.toEntity(userId));
        return new ResponseEntity<>(saved.toResponse(), HttpStatus.CREATED);
    }

    public ResponseEntity<Page<RecipeResponse>> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<RecipeResponse> dtoPage = repository.findAll(pageRequest).map(Recipe::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    public ResponseEntity<RecipeResponse> getById(UUID id) {
        Recipe data = repository.findById(id).orElseThrow(RecipeNotFoundException::new);
        return new ResponseEntity<>(data.toResponse(), HttpStatus.OK);
    }

    public ResponseEntity<RecipeResponse> update(UUID id, UUID userId, RecipeRequest request) {
        Recipe existing = repository.findById(id).orElseThrow(RecipeNotFoundException::new);

        existing.updateEntityFromRequest(request);
        Recipe saved = repository.save(existing);

        return new ResponseEntity<>(saved.toResponse(), HttpStatus.OK);
    }

    public ResponseEntity<RecipeResponse> delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new RecipeNotFoundException();
        }

        repository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<String> markAsFavourite(UUID recipeId, UUID userId) {
        RecipeFavourite favourite = new RecipeFavourite(userId, recipeId, LocalDateTime.now());

        favouriteRepository.save(favourite);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<String> removeFavourite(UUID recipeId, UUID userId) {
        favouriteRepository.deleteByUserIdAndRecipeId(userId, recipeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<List<RecipeResponse>> getAllFavourites(UUID userId) {
        List<UUID> favouriteIds = favouriteRepository.findByUserId(userId)
                .orElseThrow(() -> new RecipeNotFoundException("Favourite recipes not found"))
                .stream().map(RecipeFavourite::getRecipeId).toList();

        List<RecipeResponse> mapped = repository.findAllById(favouriteIds).stream().map(Recipe::toResponse).toList();

        return new ResponseEntity<>(mapped, HttpStatus.OK);
    }

    public ResponseEntity<String> report(UUID foodId, UUID userId, RecipeReportCreateRequest request) {
        RecipeReport report = request.toEntity(foodId, userId);

        reportRepository.save(report);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<String> reviewReport(UUID reportId, UUID userId, RecipeReportReviewRequest request) {
        RecipeReport existing = reportRepository.findById(reportId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe report not found"));

        existing.reportUpdateRequestToEntity(userId, request);
        reportRepository.save(existing); // No need to return anything

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Page<RecipeReportResponse>> getAllReports(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<RecipeReportResponse> dtoPage = reportRepository.findAll(pageRequest).map(RecipeReport::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }
}