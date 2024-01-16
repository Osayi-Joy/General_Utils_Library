package com.osayijoy.common_utils_library.registhentication.exceptions;


import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DefaultExceptionHandler implements ExceptionHandler<String,String, HttpStatus, String> {

    @Override
    public void processBadRequestException(String message, String code, String exceptionMessage) {
        log.error(" A bad request exception was thrown, with cause of error to be : {}",exceptionMessage);
       throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST, new ApiError(message,code));
    }

    @Override
    public ZeusRuntimeException processBadRequestException(String message, String code) {
        log.error(" A bad request exception was thrown, with cause of error to be : {}",code);
        return new ZeusRuntimeException(HttpStatus.BAD_REQUEST, new ApiError(message,code));
    }

    @Override
    public void processInternalException(String message, String code,String exceptionMessage) {
        log.error(" An internal exception was thrown, with cause of error to be : {}",exceptionMessage);
        throw new ZeusRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, new ApiError(message,code));
    }

    @Override
    public void processCustomException(String message, String code, HttpStatus httpStatus,String exceptionMessage) {
        log.error(" A custom exception was thrown, with cause of error to be : {}",exceptionMessage);
        throw new ZeusRuntimeException(httpStatus, new ApiError(message,code));
    }

    @Override
    public ZeusRuntimeException processCustomException(String message, String code, HttpStatus httpStatus) {
        log.error(" A custom exception was thrown, with cause of error to be : {}",code);
        throw new ZeusRuntimeException(httpStatus, new ApiError(message,code));
    }





}
