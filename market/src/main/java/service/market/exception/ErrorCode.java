package service.market.exception;

// all instances of an enum are created the moment the class loads
// unlike normal classes, you can not instantiate new instances outside of the class definition
public enum ErrorCode {
    COMPANY_NOT_FOUND,
    INVALID_COMPANY_ID,
}
