package com.osayijoy.common_utils_library.processor.processors;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.osayijoy.common_utils_library.processor.constants.ErrorConstants.*;

@Component
@Slf4j
public class RequestHandlers {

    private final Map<String, RequestHandle> requestHandleMap = new HashMap<>();

    public void addHandler(String requestType, RequestHandle requestHandle) {
        if (this.requestHandleMap.containsKey(requestType)) {
            log.error(PRETTY_PRINTER,MULTIPLE_HANDLER_FOR_REQUEST_FOUND_MESSAGE.concat(requestType));
            throw new ZeusRuntimeException(new ApiError(REQUEST_PROCESSOR_ERROR_MESSAGE, REQUEST_PROCESSOR_ERROR_CODE));
        }
        this.requestHandleMap.put(requestType, requestHandle);
    }

    public <T, V> V handle(String requestType, T request, Class<V> responseClazz)  {
        RequestHandle handler = getHandlerForRequest(requestType);
        return handler.handle(request, responseClazz);
    }

    public <V> V handle(String requestType, Class<V> responseClazz, Object... args)  {
        RequestHandle handler = getHandlerForRequest(requestType);
        return handler.handle(responseClazz, args);
    }

    private RequestHandle getHandlerForRequest(String requestType)  {
        RequestHandle handler = requestHandleMap.get(requestType);
        if (handler == null) {
            log.error(PRETTY_PRINTER,OPERATION_NOT_SUPPORTED_MESSAGE.concat(requestType));
            throw new ZeusRuntimeException(new ApiError(REQUEST_PROCESSOR_ERROR_MESSAGE, REQUEST_PROCESSOR_ERROR_CODE));
        }
        return handler;
    }

    public static class RequestHandle {
        private final Method method;
        private final Object target;

        public RequestHandle(Method method, Object target) {
            this.method = method;
            this.target = target;
        }

        @SneakyThrows
        public <V> V handle(Class<V> responseClazz, Object... args)  {
                return responseClazz.cast(method.invoke(target, args));

        }

        @SneakyThrows
        public <T, V> V handle(T request, Class<V> responseClazz)   {
                if (isVoidMethodType()) {
                    return responseClazz.cast(method.invoke(target));
                }
                return responseClazz.cast(method.invoke(target, request));

        }

        private boolean isVoidMethodType() {
            return method.getParameterCount() == 0;
        }
    }
}
