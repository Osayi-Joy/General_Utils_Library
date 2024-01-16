package com.osayijoy.common_utils_library.processor.processors;


import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import com.osayijoy.common_utils_library.commonUtils.util.ClientUtil;
import com.osayijoy.common_utils_library.commonUtils.util.FileUtils;
import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;
import com.osayijoy.common_utils_library.processor.annotations.MakerChecker;
import com.osayijoy.common_utils_library.processor.approval_repository.ApprovalRequestsRepository;
import com.osayijoy.common_utils_library.processor.approval_repository.PendingFileRequestRepository;
import com.osayijoy.common_utils_library.processor.dto.ApprovalRequestsDTO;
import com.osayijoy.common_utils_library.processor.dto.PendingFileRequestDTO;
import com.osayijoy.common_utils_library.processor.enums.ApprovalRequestStatus;
import com.osayijoy.common_utils_library.processor.model.ApprovalRequests;
import com.osayijoy.common_utils_library.processor.model.ApprovalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "makerchecker", havingValue = "true")
@EntityScan("com.digicore.request.processor.model")
@EnableJpaRepositories(basePackages = {"com.digicore.request.processor.approval_repository"})
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class MakerCheckerProcessor {

    private final ApprovalRequestsRepository requestsRepository;

    private final FileUtils fileUtils;

    private final PendingFileRequestRepository fileRequestRepository;

    private static MakerChecker getValuesPassedToMakerCheckerAnnotation(ProceedingJoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        return method.getAnnotation(MakerChecker.class);

    }

    private static Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    private static String getRequestType(ProceedingJoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        log.trace("got the request type : {}", method.getName());
        return method.getName();

    }

    private static Object[] getMethodActualArgs(ProceedingJoinPoint joinPoint, MakerChecker makerChecker, int secondToLastIndex, int lastIndex, ApprovalRequests approvalRequests) {
        try {
            Class<?> c = Class.forName(makerChecker.requestClassName());
            Object requestArg = ClientUtil.getObjectMapper().readValue(approvalRequests.getDataToUpdate(), c);
            Object[] args = joinPoint.getArgs();
            PendingFileRequestDTO[] pendingFileRequestDTOS = new PendingFileRequestDTO[approvalRequests.getPendingFileRequests().size()];
            AtomicInteger atomicInteger = new AtomicInteger(0);
            approvalRequests.getPendingFileRequests().forEach(x -> {
                PendingFileRequestDTO pendingFileRequestDTO = new PendingFileRequestDTO();
                pendingFileRequestDTO.setDocumentType(x.getDocumentType());
                pendingFileRequestDTO.setPathToFile(x.getPathToFile());
                pendingFileRequestDTOS[atomicInteger.getAndIncrement()] = pendingFileRequestDTO;
            });
            args[secondToLastIndex] = requestArg;
            args[lastIndex] = pendingFileRequestDTOS;


            return args;
        } catch (Exception e) {
            throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError("Kindly reach out to admin for help","MKT_003"));
        }
    }

    private static Result getData(ProceedingJoinPoint joinPoint) {
        int argLength = joinPoint.getArgs().length;
        String initialData;
        String dataToUpdate;

        switch (argLength) {
            case 4 -> {
                /* if case is true
                  Argument must contain the following
                  0 An initial data
                  1 A new data
                  2 An action description ( used for logging )
                  3 File array
                 */
                initialData = new JSONObject(joinPoint.getArgs()[argLength - 4]).toString();
                dataToUpdate = new JSONObject(joinPoint.getArgs()[argLength - 3]).toString();
            }
            case 3 -> {
                /* if case is true
                  Argument must contain the following
                  0 A new data
                  1 An action description ( used for logging )
                  2 File array
                 */
                initialData = "N/A";
                dataToUpdate = new JSONObject(joinPoint.getArgs()[argLength - 3]).toString();
            }
            case 2 -> {
                /* if case is true
                  Argument must contain the following
                  0 A new data
                  1 An action description ( used for logging )
                 */
                initialData = "N/A";
                dataToUpdate = new JSONObject(joinPoint.getArgs()[argLength - 2]).toString();
            }
            default -> throw new ZeusRuntimeException("invalid method arguments supplied");
        }
        return new Result(dataToUpdate, initialData);
    }

    @Around("@annotation(com.osayijoy.common_utils_library.processor.annotations.MakerChecker)")
    public Object process(ProceedingJoinPoint joinPoint) {
        MakerChecker makerChecker = getValuesPassedToMakerCheckerAnnotation(joinPoint);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(x -> x.getAuthority().contains(makerChecker.checkerPermission())))
            return checkerOperationResponse(joinPoint, auth.getName(), makerChecker);
        else if (auth.getAuthorities().stream().anyMatch(x -> x.getAuthority().contains(makerChecker.makerPermission())))
            return makerOperationResponse(joinPoint, auth.getName(), makerChecker);
        else
            throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError("You are not authorized","MKT_003"));
    }

    private Object checkerOperationResponse(ProceedingJoinPoint joinPoint, String checker, MakerChecker makerChecker) {
        int argLength = joinPoint.getArgs().length;
        ApprovalRequestsDTO requestData = (ApprovalRequestsDTO) joinPoint.getArgs()[argLength - 2];
        ApprovalRequests approvalRequests = requestsRepository.findById(requestData.getId()).orElseThrow();
        if (approvalRequests.getTreatedDate() != null) {
            throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError("Request already treated","MKT_001"));
        }


        Object[] args = getMethodActualArgs(joinPoint, makerChecker, argLength - 2, argLength - 1, approvalRequests);

        try {
            joinPoint.proceed(args);
        } catch (Throwable e) {
            try {
                throw (ZeusRuntimeException) e;
            }catch (ClassCastException exception){
                log.error("<<< error occurred while processing approval, see message : {} >>>",e.getMessage());
                throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError("Kindly reach out to admin for help","MKT_002"));
            }

        }
        approvalRequests.setApproved(true);
        approvalRequests.setApprovalUsername(checker);
        approvalRequests.setTreatedDate(LocalDateTime.now());
        approvalRequests.setStatus(ApprovalRequestStatus.EXECUTED);
        requestsRepository.save(approvalRequests);
        return getApprovalResponse("Request treated successfully", ApprovalRequestStatus.TREATED);
    }

    private Object makerOperationResponse(ProceedingJoinPoint joinPoint, String maker, MakerChecker makerChecker) {
      //  Result result = getData(joinPoint);
        int argLength = joinPoint.getArgs().length;
        String dataToUpdate = ClientUtil.getGsonMapper().toJson(joinPoint.getArgs()[argLength-2]);


        ApprovalRequests approvalRequest = new ApprovalRequests();
        approvalRequest.setStatus(ApprovalRequestStatus.NOT_TREATED);
        approvalRequest.setCreatedOn(LocalDateTime.now());
        approvalRequest.setApproved(false);
        approvalRequest.setDataToUpdate(dataToUpdate);
        approvalRequest.setInitialData("N/A");
        approvalRequest.setPermission(makerChecker.checkerPermission());
        approvalRequest.setRequesterUsername(maker);
        approvalRequest.setApprovalRequestType(getRequestType(joinPoint));
        approvalRequest.setCreatedOn(LocalDateTime.now());

        requestsRepository.save(approvalRequest);

//        if (makerChecker.requestContainsFile()){
//            PendingFileRequestDTO[] pendingFileRequestDTOS = (PendingFileRequestDTO[]) joinPoint.getArgs()[argLength-1];
//
//            Stream.of(pendingFileRequestDTOS).forEach(x-> {
//                try {
//                    savePendingFileRequest(x.getFile(),x.getDocumentType(),approvalRequest, x.getFileName());
//                } catch (BadRequestApiException e) {
//                    throw new ZeusRuntimeException(e.getMessage());
//                }
//            });
//            requestsRepository.save(approvalRequest);
//        }

        return getApprovalResponse("Request submitted successfully", ApprovalRequestStatus.SUBMITTED);
    }

    private ApprovalResponse getApprovalResponse(String description, ApprovalRequestStatus approvalRequestStatus) {
        return ApprovalResponse.builder()
                .description(description)
                .requestStatus(approvalRequestStatus)
                .build();
    }

    private record Result(String dataToUpdate, String initialData) {
    }


//    private void savePendingFileRequest(MultipartFile file, String documentType, ApprovalRequests approvalRequests,String fileName)  {
//        if (file != null && !StringUtils.isEmpty(documentType)) {
//            log.info("<<<< about to save files >>>>");
//            final String newFileName = fileUtils.fileName(fileName, LocalDate.now(), file);
//            final String pathToFile = fileUtils.saveFile(documentType, file, newFileName);
//            PendingFileRequest pendingFileRequest = new PendingFileRequest();
//            pendingFileRequest.setPathToFile(pathToFile);
//            pendingFileRequest.setApprovalRequests(approvalRequests);
//            pendingFileRequest.setDocumentType(documentType);
//            Collection<PendingFileRequest> pendingFileRequests = approvalRequests.getPendingFileRequests();
//            if (pendingFileRequests !=null)
//                pendingFileRequests.add(pendingFileRequest);
//            else {
//                pendingFileRequests = new ArrayList<>();
//                pendingFileRequests.add(pendingFileRequest);
//            }
//            fileRequestRepository.saveAll(pendingFileRequests);
//        }
//    }
}
