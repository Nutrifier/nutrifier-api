package fi.nutrifier.services;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.*;
import fi.nutrifier.mappers.RecipeMapper;
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
    private final RecipeMapper mapper;

    @Autowired
    public RecipeService(
            RecipeRepository repository,
            RecipesFavouriteRepository favouriteRepository,
            RecipeReportRepository reportRepository,
            RecipeMapper mapper
    ) {
        this.repository = repository;
        this.favouriteRepository = favouriteRepository;
        this.reportRepository = reportRepository;
        this.mapper = mapper;
    }

    public ResponseEntity<RecipeResponse> create(RecipeRequest request, UUID userId) {
        try {
            Recipe saved = repository.save(mapper.toEntity(userId, request));
            return new ResponseEntity<>(mapper.toResponse(saved), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<RecipeResponse>> getAll(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Recipe> foodPage = repository.findAll(pageRequest);

            Page<RecipeResponse> dtoPage = foodPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<RecipeResponse> getById(UUID id) {
        try {
            Recipe data = repository.findById(id).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(mapper.toResponse(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<RecipeResponse> update(UUID id, UUID userId, RecipeRequest request) {
        try {
            Recipe existing = repository.findById(id).orElse(null);

            if (existing != null) {
                mapper.updateEntityFromRequest(request, existing);

                Recipe saved = repository.save(existing);
                return new ResponseEntity<>(mapper.toResponse(saved), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println("service error: " + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<RecipeResponse> delete(UUID id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> markAsFavourite(UUID recipeId, UUID userId) {
        try {
            RecipeFavourite favourite = new RecipeFavourite(userId, recipeId, LocalDateTime.now());
            favouriteRepository.save(favourite);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> removeFavourite(UUID recipeId, UUID userId) {
        try {
            favouriteRepository.deleteByUserIdAndRecipeId(userId, recipeId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<RecipeResponse>> getAllFavourites(UUID userId) {
        try {
            List<RecipeFavourite> favourites = favouriteRepository.findByUserId(userId);
            List<Recipe> foods = repository.findAllById(favourites.stream().map(RecipeFavourite::getRecipeId).toList());
            List<RecipeResponse> mapped = foods.stream().map(mapper::toResponse).toList();

            return new ResponseEntity<>(mapped, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> report(UUID foodId, UUID userId, RecipeReportCreateRequest request) {
        try {
            RecipeReport report = mapper.reportCreateRequestToEntity(foodId, userId, request);
            reportRepository.save(report);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> reviewReport(UUID reportId, UUID userId, RecipeReportReviewRequest request) {
        try {
            RecipeReport existing = reportRepository.findById(reportId).orElse(null);

            if (existing != null) {
                mapper.reportUpdateRequestToEntity(userId, request, existing);

                // No need to return anything
                reportRepository.save(existing);

                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<RecipeReportResponse>> getAllReports(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<RecipeReport> recipeReportPage = reportRepository.findAll(pageRequest);

            Page<RecipeReportResponse> dtoPage = recipeReportPage.map(mapper::reportEntityToResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}