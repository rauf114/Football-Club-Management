package com.example.footballclubmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.example.footballclubmanagement.util.Constants.BAD_REQUEST;
import static com.example.footballclubmanagement.util.Constants.SUCCESS;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private int responseStatus;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(SUCCESS)
                .responseStatus(HttpStatus.OK.value())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message != null ? message : SUCCESS)
                .responseStatus(status.value())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message != null ? message : BAD_REQUEST)
                .responseStatus(status.value())
                .data(null)
                .build();
    }
}