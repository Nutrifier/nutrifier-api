package fi.nutrifier.enums;

import org.springframework.http.HttpStatus;

public enum ResponseCode {
    GENERAL_ERROR,
    CONSTRAINT_VIOLATION,

    DAILY_SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND),
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND),
    GOALS_NOT_FOUND(HttpStatus.NOT_FOUND),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND),
    WEIGHT_ENTRY_NOT_FOUND(HttpStatus.NOT_FOUND),
    MEAL_NOT_FOUND(HttpStatus.NOT_FOUND),
    RECIPE_NOT_FOUND(HttpStatus.NOT_FOUND),
    SETTINGS_NOT_FOUND(HttpStatus.NOT_FOUND),
    USER_FEEDBACK_NOT_FOUND(HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    CRYPTION_ERROR,
    ENCRYPTION_KEY_ERROR,

    MACRO_TO_CALORIE_CALCULATION_DIFFERED_FROM_INPUTTED_CALORIES(HttpStatus.OK);

    private final HttpStatus httpStatus;

    ResponseCode() {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    ResponseCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
