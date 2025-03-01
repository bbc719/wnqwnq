package com.bside.potenday.domain.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResult<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ApiResult<T> successResponse(int status, T data){
        return new ApiResult<>(status, null, data);
    }

    public static <T> ApiResult<T> successWithoutResponse(int status){
        return new ApiResult<>(status, null, null);
    }

    public static <T> ApiResult<T> error(int status, String message){
        return new ApiResult<>(status, message, null);
    }

    private ApiResult(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}