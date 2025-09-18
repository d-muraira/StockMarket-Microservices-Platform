package service.exceptions;

/**
 * Representation of an API error response
 */
public class ApiError {
    public ErrorCode code;
    public String message;

    public ApiError(ErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }
}