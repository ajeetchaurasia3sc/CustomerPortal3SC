package com.sssupply.customerportal.dto;
import lombok.*;
import java.util.Map;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private Map<String, Object> meta;
    private Object errors;
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder().success(true).data(data).build();
    }
    public static <T> ApiResponse<T> success(T data, Map<String, Object> meta) {
        return ApiResponse.<T>builder().success(true).data(data).meta(meta).build();
    }
    public static <T> ApiResponse<T> error(Object errors) {
        return ApiResponse.<T>builder().success(false).errors(errors).build();
    }
}
