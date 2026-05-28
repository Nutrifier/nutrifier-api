package fi.nutrifier.exceptions;

import fi.nutrifier.dto.ApiResponse;
import fi.nutrifier.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(FoodNotFoundException.class)
    public ResponseEntity<?> handleFoodNotFound(
            FoodNotFoundException ex
    ) {
        log.error("Food not found exception", ex);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DailySummaryNotFoundException.class)
    public ResponseEntity<?> handleDailySummaryNotFound(
            DailySummaryNotFoundException ex
    ) {
        log.error("Daily summary not found exception", ex);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GoalsNotFoundException.class)
    public ResponseEntity<?> handleGoalsNotFound(
            GoalsNotFoundException ex
    ) {
        log.error("Goals not found exception", ex);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<?> handleProfileNotFound(
            ProfileNotFoundException ex
    ) {
        log.error("Profile not found exception", ex);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WeightEntryNotFoundException.class)
    public ResponseEntity<?> handleWeightEntryNotFound(
            WeightEntryNotFoundException ex
    ) {
        log.error("Weight entry not found exception", ex);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MealNotFoundException.class)
    public ResponseEntity<?> handleMealNotFound(
            MealNotFoundException ex
    ) {
        log.error("Meal not found exception", ex);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<?> handleRecipeNotFound(
            RecipeNotFoundException ex
    ) {
        log.error("Recipe not found exception", ex);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SettingsNotFoundException.class)
    public ResponseEntity<?> handleSettingsNotFound(
            SettingsNotFoundException ex
    ) {
        log.error("Settings not found exception", ex);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserFeedbackNotFoundException.class)
    public ResponseEntity<?> handleUserFeedbackNotFound(
            UserFeedbackNotFoundException ex
    ) {
        log.error("User feedback not found exception", ex);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(
            UserNotFoundException ex
    ) {
        log.error("User not found exception", ex);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(
            DataIntegrityViolationException ex
    ) {
        log.error("Database constraint violation", ex);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FailedCryptionException.class)
    public ResponseEntity<?> handleFailedCryptionException(
            FailedCryptionException ex
    ) {
        log.error("Cryption exception occurred", ex);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EncryptionKeyException.class)
    public ResponseEntity<?> handleEncryptionKeyException(
            EncryptionKeyException ex
    ) {
        log.error("Encryption key exception occurred", ex);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        log.error("Validation exception occurred", ex);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(
            Exception ex
    ) {
        log.error("Unhandled exception occurred", ex);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
