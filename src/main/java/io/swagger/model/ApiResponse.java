package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper class to return both the data (if present) and a message.
 * This is useful if errors happen.
 * @param <T> The data type.
 */
public class ApiResponse<T> {

    @JsonProperty("message")
    public String message;

    @JsonProperty("data")
    public T data;

    /**
     * Constructor.
     *
     * @param message The message.
     * @param data The data to be returned.
     */
    public ApiResponse(String message, T data) {
        this.data = data;
        this.message = message;
    }

    public ApiResponse(String message) {
        this(message, null);
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
