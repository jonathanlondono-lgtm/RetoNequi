package co.com.nequi.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_PARAMETERS("GEN-002", "Bad Parameters, please verify data", 400),
    DATABASE_ERROR("TEC-001", "Database Error, please try again", 500),

    FRANCHISE_ALREADY_EXITS("FRN-001", "Franchise already exists", 409),
    FRANCHISE_NOT_FOUND("FRN-002", "Franchise not found: %s", 404),

    BRANCH_ALREADY_EXISTS("BRN-003", "Branch already exists in this franchise", 409),
    BRANCH_NOT_FOUND("BRN-001", "Branch not found: %s", 404),

    PRODUCT_NOT_FOUND("PRD-001", "Product not found: %s", 404),
    PRODUCT_ALREADY_EXISTS("PRD-003", "Product already exists in this branch", 409);


    private final String code;
    private  final String message;
    private final int statusCode;
}
