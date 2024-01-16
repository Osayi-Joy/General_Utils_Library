package com.osayijoy.common_utils_library.processor.processors;


import com.osayijoy.common_utils_library.helper.response.ApiError;
import com.osayijoy.common_utils_library.helper.response.ApiResponseJson;
import com.osayijoy.common_utils_library.processor.dto.ApprovalRequestsDTO;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "makerchecker", havingValue = "true")
@Slf4j
@Component
public class ApprovalRequestProcessor {

    private RequestHandlers requestHandlers;

    private final RequestHandlerPostProcessor requestHandlerPostProcessor;
    public ApprovalRequestProcessor(RequestHandlerPostProcessor requestHandlerPostProcessor) {
        this.requestHandlerPostProcessor = requestHandlerPostProcessor;
    }

    @PostConstruct
    public void init() {
        requestHandlers = requestHandlerPostProcessor.getHandlers();
    }


    public Object process(ApprovalRequestsDTO approvalRequest){
        if (approvalRequest == null) {
            return ApiResponseJson.builder()
                    .data(null)
                    .message("Invalid Request")
                    .errors(List.of(new ApiError("Invalid Request","99")))
                    .build();        }
           log.trace("Approval request processor request type is {}", approvalRequest.getApprovalRequestType());

            Object response = requestHandlers.handle(approvalRequest.getApprovalRequestType(), Object.class,approvalRequest);
            log.trace("Approval request processor response {}", response);
            return response;

    }


}
