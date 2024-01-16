package com.osayijoy.common_utils_library.processor.processors;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.osayijoy.common_utils_library.processor.model.PendingFileRequest;
import com.osayijoy.common_utils_library.commonUtils.util.BeanUtilWrapper;
import com.osayijoy.common_utils_library.commonUtils.util.ClientUtil;
import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;
import com.osayijoy.common_utils_library.processor.annotations.RequestHandler;
import com.osayijoy.common_utils_library.processor.annotations.RequestType;
import com.osayijoy.common_utils_library.processor.approval_repository.ApprovalRequestsRepository;
import com.osayijoy.common_utils_library.processor.approval_repository.PendingFileRequestRepository;
import com.osayijoy.common_utils_library.processor.dto.ApprovalRequestsDTO;
import com.osayijoy.common_utils_library.processor.enums.ApprovalRequestStatus;
import com.osayijoy.common_utils_library.processor.enums.RequestHandlerType;
import com.osayijoy.common_utils_library.processor.model.ApprovalRequests;
import com.osayijoy.common_utils_library.processor.model.ApprovalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@ConditionalOnProperty(name = "makerchecker", havingValue = "true")
@RequiredArgsConstructor
@RequestHandler(type = RequestHandlerType.PROCESS_MAKER_REQUESTS)
public class ApprovalRequestService {

    private final ApprovalRequestsRepository requestsRepository;

    private final PendingFileRequestRepository fileRequestRepository;

    private final ApprovalRequestProcessor approvalRequestProcessor;

    private static final String INVALID_REQUEST_ID_MESSAGE = "Invalid request id supplied";
    private static final String INVALID_REQUEST_ID_CODE = "MK_001";

    private static final String REQUEST_TREATED_MESSAGE = "Request already treated";
    private static final String REQUEST_TREATED_CODE = "MK_002";

    @RequestType(name = "approveRequest")
    public Object approveRequest(Object requestsDTO){
       ApprovalRequestsDTO requestDTO = (ApprovalRequestsDTO) requestsDTO;
        ApprovalRequests approvalRequests = requestsRepository.findById(requestDTO.getId()).orElseThrow(()-> new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError(INVALID_REQUEST_ID_MESSAGE,INVALID_REQUEST_ID_CODE)));
        if (!ApprovalRequestStatus.NOT_TREATED.equals(approvalRequests.getStatus()))
            throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError(REQUEST_TREATED_MESSAGE,REQUEST_TREATED_CODE));
        ApprovalRequestsDTO approvalRequestsDTO = new ApprovalRequestsDTO();
        BeanUtilWrapper.copyNonNullProperties(approvalRequests,approvalRequestsDTO);
       return approvalRequestProcessor.process(approvalRequestsDTO);

    }

    public Object getRequest(Long id){
        ApprovalRequests approvalRequests = requestsRepository.findById(id).orElseThrow(()-> new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError(INVALID_REQUEST_ID_MESSAGE,INVALID_REQUEST_ID_CODE)));
        ApprovalRequestsDTO approvalRequestsDTO = new ApprovalRequestsDTO();
        BeanUtilWrapper.copyNonNullProperties(approvalRequests,approvalRequestsDTO);
        return approvalRequestsDTO;

    }

    @RequestType(name = "declineRequest")
    public Object declineRequest(Object requestsDTO){
        ApprovalRequestsDTO requestDTO = (ApprovalRequestsDTO) requestsDTO;
        ApprovalRequests approvalRequests = requestsRepository.findById(requestDTO.getId()).orElseThrow(()-> new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError(INVALID_REQUEST_ID_MESSAGE,INVALID_REQUEST_ID_CODE)));
        if (!ApprovalRequestStatus.NOT_TREATED.equals(approvalRequests.getStatus()))
            throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError("Request already declined","MK_003"));
        BeanUtilWrapper.copyNonNullProperties(approvalRequests,requestDTO);
        if(approvalRequests.getApprovalRequestType().equals("approveDisputeRejection")
                || approvalRequests.getApprovalRequestType().equals("declineDisputeRejection")
                || approvalRequests.getApprovalRequestType().equals("approveDisputeManualHandling")
                || approvalRequests.getApprovalRequestType().equals("declineDisputeManualHandling")) {
            requestDTO.setApprovalRequestType("decline".concat(ClientUtil.capitalizeFirstLetter(requestDTO.getApprovalRequestType())));
            approvalRequestProcessor.process(requestDTO);
        }
        approvalRequests.setApproved(false);
        approvalRequests.setStatus(ApprovalRequestStatus.DECLINED);
        approvalRequests.setTreatedDate(LocalDateTime.now());
        approvalRequests.setApprovalUsername(ClientUtil.getLoggedInUsername());
        requestsRepository.save(approvalRequests);

        return ApprovalResponse.builder()
                .description("Request submitted successfully")
                .requestStatus(ApprovalRequestStatus.DECLINED)
                .build();
    }

   // @RequestType(name = "getPendingApprovalRequests")
    public Map<String,Object> getPendingApprovalRequests(List<String> loggedInUserPermissions, int page, int size){

        Page<ApprovalRequests> merchantProfiles = requestsRepository.findByPermissionInAndStatusOrderByCreatedOnDesc(loggedInUserPermissions,ApprovalRequestStatus.NOT_TREATED,getPageable(page,size));
        return getStringObjectMap(merchantProfiles);
    }

   // @RequestType(name = "getApprovalRequests")
    public Map<String,Object> getApprovalRequests(List<String> loggedInUserPermissions, int page, int size){

        Page<ApprovalRequests> merchantProfiles = requestsRepository.findByPermissionInAndStatusOrderByCreatedOnDesc(loggedInUserPermissions,ApprovalRequestStatus.TREATED,getPageable(page,size));
        return getStringObjectMap(merchantProfiles);
    }

    public Map<String,Object> getRequests(List<String> loggedInUserPermissions, int page, int size,ApprovalRequestStatus status ){
        Page<ApprovalRequests> merchantProfiles = requestsRepository.findByPermissionInAndStatusOrderByCreatedOnDesc(loggedInUserPermissions,status,getPageable(page,size));
        return getStringObjectMap(merchantProfiles);
    }

    private static Map<String, Object> getStringObjectMap(Page<ApprovalRequests> merchantProfiles) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", merchantProfiles.stream().map(x->{
            ApprovalRequestsDTO approvalRequestsDTO = new ApprovalRequestsDTO();
            BeanUtilWrapper.copyNonNullProperties(x,approvalRequestsDTO);
            approvalRequestsDTO.setTreatDate(x.getTreatedDate());
            approvalRequestsDTO.setApprovalRequestType(ClientUtil.sentenceCase(x.getApprovalRequestType()));
            if (!x.getPendingFileRequests().isEmpty())
                approvalRequestsDTO.setDocumentsUploaded(x.getPendingFileRequests().stream().map(PendingFileRequest::getDocumentType).toArray());
            return approvalRequestsDTO;
        }).toList());
        response.put("currentPage", merchantProfiles.getNumber() + 1);
        response.put("totalItems", merchantProfiles.getTotalElements());
        response.put("totalPages", merchantProfiles.getTotalPages());
        response.put("isFirstPage", merchantProfiles.isFirst());
        response.put("isLastPage", merchantProfiles.isLast());
        return response;
    }

    public static Pageable getPageable(int page, int size){
        return PageRequest.of(
                Math.max(page - 1, 0),
                size);
    }


}
