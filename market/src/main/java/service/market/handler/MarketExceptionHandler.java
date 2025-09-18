package service.market.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import service.market.exception.ApiError;
import service.market.exception.CompanyNotFoundException;
import service.market.exception.ErrorCode;
import service.market.exception.InvalidCompanyIdException;

/** MarketExceptionHandler
 * Class: global exception handler
 * Purpose:
 *  - when exceptions are thrown, Spring looks inside @RestControllerAdvice class
 *  - to find the matching exception
 * ExceptionHandler annotation:
 *  - maps exception types to methods that handle them
 *  - if no matching exception handler found, Spring uses a built-in default error handler
 * Benefit:
 *  - A controller method that throws an error registered in this class do not require try catch.
 *  - ^^^ Methods defined here handle the exception (Spring catches for you)
 *  - Creates a consolidated error handling into a single class
 */

@RestControllerAdvice
public class MarketExceptionHandler {
    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<ApiError> handleCompanyNotFound(CompanyNotFoundException ex) {
        ApiError error = new ApiError(ErrorCode.COMPANY_NOT_FOUND, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InvalidCompanyIdException.class)
    public ResponseEntity<ApiError> handleInvalidCompanyId(InvalidCompanyIdException ex) {
        ApiError error = new ApiError(ErrorCode.INVALID_COMPANY_ID, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
