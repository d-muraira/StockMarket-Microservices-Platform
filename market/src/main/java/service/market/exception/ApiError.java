package service.market.exception;

public class ApiError {
    public ErrorCode code;
    public String message;

    public ApiError(ErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }
}
