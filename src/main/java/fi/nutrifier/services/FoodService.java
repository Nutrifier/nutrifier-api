package fi.nutrifier.services;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.Food;
import fi.nutrifier.entities.FoodFavourite;
import fi.nutrifier.entities.FoodReport;
import fi.nutrifier.entities.FoodUsage;
import fi.nutrifier.mappers.FoodMapper;
import fi.nutrifier.repositories.FoodFavouriteRepository;
import fi.nutrifier.repositories.FoodReportRepository;
import fi.nutrifier.repositories.FoodRepository;
import fi.nutrifier.repositories.FoodUsageRepository;
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
public class FoodService {

    private final FoodRepository repository;
    private final FoodFavouriteRepository favouriteRepository;
    private final FoodReportRepository reportRepository;
    private final FoodUsageRepository usageRepository;
    private final FoodMapper mapper;

    @Autowired
    public FoodService(
            FoodRepository repository,
            FoodFavouriteRepository favouriteRepository,
            FoodReportRepository reportRepository,
            FoodUsageRepository usageRepository,
            FoodMapper mapper
    ) {
        this.repository = repository;
        this.favouriteRepository = favouriteRepository;
        this.reportRepository = reportRepository;
        this.usageRepository = usageRepository;
        this.mapper = mapper;
    }

    public ResponseEntity<FoodResponse> create(FoodRequest foodRequest, UUID userId) {
        try {
            Food saved = repository.save(mapper.toEntity(userId, foodRequest));
            return new ResponseEntity<>(mapper.toResponse(saved), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<FoodResponse>> getAll(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Food> foodPage = repository.findAll(pageRequest);

            Page<FoodResponse> dtoPage = foodPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FoodResponse> getById(UUID id) {
        try {
            Food data = repository.findById(id).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(mapper.toResponse(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FoodResponse> update(UUID id, UUID userId, FoodRequest foodRequest) {
        try {
            Food existing = repository.findById(id).orElse(null);

            if (existing != null) {
                mapper.updateEntityFromRequest(foodRequest, existing);
                existing.setUpdatedBy(userId);

                Food saved = repository.save(existing);
                return new ResponseEntity<>(mapper.toResponse(saved), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println("service error: " + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FoodResponse> delete(UUID id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<FoodResponse>> getFoodsByQuery(Integer page, Integer size, String query) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Food> foodPage = repository.findFoodsByNameContainingIgnoreCase(query, pageRequest);

            Page<FoodResponse> dtoPage = foodPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<FoodResponse>> getFoodsByBarcode(String query) {
        try {
            List<Food> foods = repository.findFoodsByBarcodeContainingIgnoreCase(query);
            List<FoodResponse> mapped = foods.stream().map(mapper::toResponse).toList();

            return new ResponseEntity<>(mapped, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<FoodResponse>> getRecentFoods(UUID userId) {
        try {
            LocalDateTime date = LocalDateTime.now().minusDays(7); // Ignoring usage logs if they are past 7 days
            List<FoodUsage> foodUsageList = usageRepository.findTop5ByUserIdAndLastUsedAtAfterOrderByLastUsedAtDesc(userId, date);

            List<UUID> foodIds = foodUsageList.stream().map(FoodUsage::getFoodId).toList();

            List<Food> foods = repository.findAllById(foodIds);

            List<FoodResponse> dtoList = foods.stream().map(mapper::toResponse).toList();

            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> markAsFavourite(UUID foodId, UUID userId) {
        try {
            FoodFavourite favourite = new FoodFavourite();
            favourite.setUserId(userId);
            favourite.setFoodId(foodId);

            // No need to return anything
            favouriteRepository.save(favourite);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> removeFavourite(UUID foodId, UUID userId) {
        try {
            favouriteRepository.deleteByUserIdAndFoodId(userId, foodId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<FoodResponse>> getAllFavourites(UUID userId) {
        try {
            List<FoodFavourite> favourites = favouriteRepository.findByUserId(userId);
            List<Food> foods = repository.findAllById(favourites.stream().map(FoodFavourite::getFoodId).toList());
            List<FoodResponse> mapped = foods.stream().map(mapper::toResponse).toList();

            return new ResponseEntity<>(mapped, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> report(UUID foodId, UUID userId, FoodReportCreateRequest request) {
        try {
            FoodReport report = mapper.reportCreateRequestToEntity(foodId, userId, request);

            // No need to return anything
            reportRepository.save(report);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> reviewReport(UUID reportId, UUID userId, FoodReportReviewRequest request) {
        try {
            FoodReport existing = reportRepository.findById(reportId).orElse(null);

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

    public ResponseEntity<Page<FoodReportResponse>> getAllReports(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<FoodReport> foodReportPage = reportRepository.findAll(pageRequest);

            Page<FoodReportResponse> dtoPage = foodReportPage.map(mapper::reportEntityToResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}