package fi.nutrifier.services;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.Food;
import fi.nutrifier.entities.FoodFavourite;
import fi.nutrifier.entities.FoodReport;
import fi.nutrifier.entities.FoodUsage;
import fi.nutrifier.enums.ResponseCode;
import fi.nutrifier.exceptions.BarcodeAlreadyExistsException;
import fi.nutrifier.exceptions.FoodNotFoundException;
import fi.nutrifier.repositories.FoodFavouriteRepository;
import fi.nutrifier.repositories.FoodReportRepository;
import fi.nutrifier.repositories.FoodRepository;
import fi.nutrifier.repositories.FoodUsageRepository;
import fi.nutrifier.utils.CalculationUtil;
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

    @Autowired
    public FoodService(
            FoodRepository repository,
            FoodFavouriteRepository favouriteRepository,
            FoodReportRepository reportRepository,
            FoodUsageRepository usageRepository
    ) {
        this.repository = repository;
        this.favouriteRepository = favouriteRepository;
        this.reportRepository = reportRepository;
        this.usageRepository = usageRepository;
    }

    public ResponseEntity<FoodResponse> create(FoodRequest foodRequest, UUID userId) {
        double calculatedCalories = CalculationUtil.calculateCaloriesFromMacros(
                foodRequest.getFat(),
                foodRequest.getCarbs(),
                foodRequest.getProtein()
        );

        double calorieSpreadMax = calculatedCalories + 100;
        double calorieSpreadMin = calculatedCalories - 100;

        Food saved = repository.save(foodRequest.toEntity(userId));
        FoodResponse response = saved.toResponse();
        response.setMessage(ResponseCode.MACRO_TO_CALORIE_CALCULATION_DIFFERED_FROM_INPUTTED_CALORIES.name());

        if (saved.getCalories() > calorieSpreadMax || saved.getCalories() < calorieSpreadMin) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<FoodResponse> addBarcode(String newBarcode, UUID foodId, UUID userId) {
        Food found = repository.findById(foodId).orElseThrow(() -> new FoodNotFoundException(foodId.toString()));

        if (found.getBarcode() != null && !found.getBarcode().isEmpty()) {
            throw new BarcodeAlreadyExistsException();
        }

        found.setBarcode(newBarcode);
        found.setUpdatedAt(LocalDateTime.now());
        found.setUpdatedBy(userId);
        Food updated = repository.save(found);

        return new ResponseEntity<>(updated.toResponse(), HttpStatus.OK);
    }

    public ResponseEntity<Page<FoodResponse>> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<FoodResponse> dtoPage = repository.findAll(pageRequest).map(Food::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    public ResponseEntity<List<FoodResponse>> getByIds(List<UUID> ids) {
        List<Food> data = repository.findAllById(ids);

        if (data.isEmpty()) {
            throw new FoodNotFoundException();
        }

        return new ResponseEntity<>(data.stream().map(Food::toResponse).toList(), HttpStatus.OK);
    }

    public ResponseEntity<FoodResponse> update(UUID id, UUID userId, FoodRequest foodRequest) {
        Food existing = repository.findById(id).orElseThrow(FoodNotFoundException::new);

        existing.updateEntityFromRequest(foodRequest);
        existing.setUpdatedBy(userId);

        Food saved = repository.save(existing);

        return new ResponseEntity<>(saved.toResponse(), HttpStatus.OK);
    }

    public ResponseEntity<String> delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new FoodNotFoundException();
        }

        repository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Page<FoodResponse>> getFoodsByQuery(Integer page, Integer size, String query) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<FoodResponse> dtoPage = repository
                .findFoodsByNameContainingIgnoreCase(query, pageRequest)
                .map(Food::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    public ResponseEntity<List<FoodResponse>> getFoodsByBarcode(String query) {
        List<FoodResponse> mapped = repository
                .findFoodsByBarcodeContainingIgnoreCase(query)
                .stream()
                .map(Food::toResponse)
                .toList();

        return new ResponseEntity<>(mapped, HttpStatus.OK);
    }

    public ResponseEntity<List<FoodResponse>> getRecentFoods(UUID userId) {
        LocalDateTime date = LocalDateTime.now().minusDays(7); // Ignoring usage logs if they are past 7 days
        List<FoodUsage> usages = usageRepository.findTop5ByUserIdAndLastUsedAtAfterOrderByLastUsedAtDesc(userId, date);

        List<UUID> foodIds = usages.stream().map(FoodUsage::getFoodId).toList();

        List<Food> foods = repository.findAllById(foodIds);

        List<FoodResponse> dtoList = foods.stream().map(Food::toResponse).toList();

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    public ResponseEntity<String> markAsFavourite(UUID foodId, UUID userId) {
        FoodFavourite favourite = new FoodFavourite();
        favourite.setUserId(userId);
        favourite.setFoodId(foodId);

        // No need to return anything
        favouriteRepository.save(favourite);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<String> removeFavourite(UUID foodId, UUID userId) {
        favouriteRepository.deleteByUserIdAndFoodId(userId, foodId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<List<FoodResponse>> getAllFavourites(UUID userId) {
        List<UUID> favouriteIds = favouriteRepository
                .findByUserId(userId)
                .stream()
                .map(FoodFavourite::getFoodId)
                .toList();

        List<FoodResponse> mapped = repository.findAllById(favouriteIds).stream().map(Food::toResponse).toList();

        return new ResponseEntity<>(mapped, HttpStatus.OK);
    }

    public ResponseEntity<String> report(UUID foodId, UUID userId, FoodReportCreateRequest request) {
        FoodReport report = request.toEntity(foodId, userId);

        // No need to return anything
        reportRepository.save(report);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<String> reviewReport(UUID reportId, UUID userId, FoodReportReviewRequest request) {
        FoodReport existing = reportRepository.findById(reportId)
                .orElseThrow(() -> new FoodNotFoundException("Food report not found"));

        existing.reportUpdateRequestToEntity(userId, request);
        reportRepository.save(existing);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Page<FoodReportResponse>> getAllReports(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<FoodReportResponse> dtoPage = reportRepository.findAll(pageRequest).map(FoodReport::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }
}