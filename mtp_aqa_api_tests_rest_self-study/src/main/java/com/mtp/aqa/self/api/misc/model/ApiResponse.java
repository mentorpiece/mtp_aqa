package com.mtp.aqa.self.api.misc.model;

/**
 * {
 * "code": 200,
 * "type": "unknown",
 * "message": "9223372036854775807"
 * }
 */
public class ApiResponse {
    private long code;
    private String type;
    private String message;

    public ApiResponse() {
    }

    public ApiResponse(long code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
