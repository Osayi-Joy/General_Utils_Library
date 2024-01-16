package com.osayijoy.common_utils_library.helper.controlleradvice;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.*;

import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;
import com.osayijoy.common_utils_library.helper.response.ApiResponseJson;
import com.osayijoy.common_utils_library.helper.response.ControllerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static com.osayijoy.common_utils_library.helper.constant.StatusCode.FILE_LIMIT_EXCEEDED_CODE;
import static com.osayijoy.common_utils_library.helper.constant.StatusCode.FILE_NOT_FOUND_CODE;

@ControllerAdvice
@Slf4j
public class ZeusControllerAdvice {


    @ExceptionHandler({IOException.class})
    public ResponseEntity<Object> handleFileNotFoundException(
            IOException exception, WebRequest request) {
        List<ApiError> apiErrors = new ArrayList<>();
        apiErrors.add(new ApiError("file not found", FILE_NOT_FOUND_CODE));
        ApiResponseJson<Object> responseJson =
                ApiResponseJson.builder()
                        .data(null)
                        .errors(apiErrors)
                        .message("file requested not found")
                        .build();
        return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleZeusRuntimeException(
            SQLIntegrityConstraintViolationException exception) {
        ApiError error =
                new ApiError(
                        "Seems some of the information supplied are not valid, Kindly contact support for help",
                        "45");
        ApiResponseJson<String> responseJson =
                ApiResponseJson.<String>builder()
                        .data(null)
                        .errors(new ArrayList<>())
                        .message(
                                "Seems some of the information supplied are not valid, Kindly contact support for help")
                        .success(false)
                        .build();
        responseJson.addError(error);

        return new ResponseEntity<>((responseJson), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<ApiError> errors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            ApiError error = new ApiError(fieldError.getDefaultMessage(), "90");
            errors.add(error);
        }

        return ControllerResponse.buildFailureResponse(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex) {
        return ControllerResponse.buildFailureResponse(
                List.of(new ApiError(ex.getMessage(), "04")), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ZeusRuntimeException.class)
    public ResponseEntity<Object> handleZeusRuntimeException(ZeusRuntimeException exception) {
        log.debug("cause of error is : {}",exception.getMessage());
        return ControllerResponse.buildFailureResponse(
                exception.getErrors(), exception.getHttpStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex) {
        return ControllerResponse.buildFailureResponse(
                List.of(new ApiError(ex.getMethod().concat(" is not supported"))),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ControllerResponse.buildFailureResponse(
                List.of(new ApiError("The required payload is missing")), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<Object> handleClassNotFoundException(ClassNotFoundException exception) {
        return ControllerResponse.buildFailureResponse(
                List.of(new ApiError("Kindly reach out to support for help", "09")),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException exception) {
        return ControllerResponse.buildFailureResponse(
                List.of(
                        new ApiError(
                                exception.getParsedString().concat(" is not in the valid date format"), "09")),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({FileUploadBase.FileSizeLimitExceededException.class})
    public ResponseEntity<Object> handleFieldExceededException(
            FileUploadBase.FileSizeLimitExceededException exception, WebRequest request) {
        List<ApiError> apiErrors = new ArrayList<>();
        apiErrors.add(new ApiError(exception.getMessage(), FILE_LIMIT_EXCEEDED_CODE));
        ApiResponseJson<Object> responseJson =
                ApiResponseJson.builder()
                        .data(null)
                        .errors(apiErrors)
                        .message(exception.getMessage())
                        .build();
        return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
    }
}
