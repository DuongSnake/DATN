package com.example.bloodbankmanagement.common.untils;

public enum ErrorCode {
    SUCCESS(200, "success", "OK"),
    FAIL(400, "fail", "Bad Request"),
    INVALID_INPUT_VALUE(400, "C00001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "fail", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    DUPLICATE_INPUT_VALUE(400, "fail", "Duplicate Input Value"),
    UsernameOrPasswordNotFoundException(400, "S001", "ID or password does not match."),
    ForbiddenException(403, "S002", "You are not authorized to make this request."),
    UNAUTHORIZEDException(401, "S003", "Available after logging in."),
    ExpiredJwtException(444, "S004", "The existing token has expired. Please go to the get-newtoken link with the token."),
    ReLogin(445, "S005", "All tokens have expired. Please log in again.");

    private final String code;
    private final String message;
    private int status;

    private ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public int getStatus() {
        return this.status;
    }
}
