package fi.nutrifier.dto;

public class ApiResponse<T> {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }
}