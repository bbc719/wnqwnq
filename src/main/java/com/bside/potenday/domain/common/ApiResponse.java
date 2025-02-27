package com.bside.potenday.domain.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> successResponse(int status, T data){
        return new ApiResponse<>(status, null, data);
    }

    public static <T> ApiResponse<T> successWithoutResponse(int status){
        return new ApiResponse<>(status, null, null);
    }

    public static <T> ApiResponse<T> error(int status, String message){
        return new ApiResponse<>(status, message, null);
    }

    private ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}