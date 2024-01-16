package com.osayijoy.common_utils_library.helper.exception;

import java.util.ArrayList;
import java.util.List;

import com.osayijoy.common_utils_library.helper.response.ApiError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-12(Mon)-2022
 */

@Getter
public class ZeusRuntimeException extends RuntimeException{


    private String code = "SER500";


    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    private List<ApiError> errors = new ArrayList<>();
    public ZeusRuntimeException() {
    }

    public ZeusRuntimeException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ZeusRuntimeException(String message, String code, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public ZeusRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ZeusRuntimeException(String message, HttpStatus httpStatus, List<ApiError> errors) {
        super(message);
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

    public ZeusRuntimeException(HttpStatus httpStatus, List<ApiError> errors) {
        super("unable to process request");
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

    public ZeusRuntimeException(HttpStatus httpStatus, ApiError error) {
        super("unable to process request");
        this.httpStatus = httpStatus;
        this.errors = List.of(error);
    }

    public ZeusRuntimeException(ApiError error) {
        super("unable to process request");
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errors = List.of(error);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public String getCode() {
        return code;
    }

    public ZeusRuntimeException(String message, Throwable inner) {
        super(message, inner);
    }

    public ZeusRuntimeException(String message) {
        super(message);
    }


}
