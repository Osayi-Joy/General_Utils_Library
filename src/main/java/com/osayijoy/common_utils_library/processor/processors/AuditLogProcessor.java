package com.osayijoy.common_utils_library.processor.processors;


import com.osayijoy.common_utils_library.commonUtils.util.ClientUtil;
import com.osayijoy.common_utils_library.processor.annotations.LogActivity;
import com.osayijoy.common_utils_library.processor.model.AuditLog;
import com.osayijoy.common_utils_library.processor.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@ConditionalOnProperty(name = "audit.logging.enable", havingValue = "true")
@EntityScan("com.osayijoy.settlement_reconciliation_lib.processor.model")
@EnableJpaRepositories(basePackages = {"com.osayijoy.settlement_reconciliation_lib.processor.repository"})
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditLogProcessor {

    private final AuditLogRepository auditLogRepository;
    private final HttpServletRequest request;



   private static LogActivity getLogActivityPassedValue(ProceedingJoinPoint joinPoint){
        Method method = getMethod(joinPoint);
        return method.getAnnotation(LogActivity.class);
   }


    private static Method getMethod(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }





    @Around("@annotation(com.osayijoy.common_utils_library.processor.annotations.LogActivity)")
    @Transactional
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        AuditLog auditLog = new AuditLog();
        auditLog.setLogStartDate(LocalDateTime.now());

        retrieveNecessaryInfoFromLoggedInUserAccessToken(auditLog);


        String userName = ClientUtil.getLoggedInUsername();
        Object object = joinPoint.proceed();
        auditLog.setActivitySuccessfullyDone(!(object instanceof Throwable));
        LogActivity logActivity = getLogActivityPassedValue(joinPoint);
        auditLog.setActivity(logActivity.activity());
        auditLog.setAuditType(logActivity.auditType());
        auditLog.setIpAddress(ClientUtil.getIpAddress(request));
        auditLog.setEmail(userName);
        auditLog.setActivityDescription(logActivity.auditDescription());
        auditLog.setLogEndDate(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    return object;
   }

    public void retrieveNecessaryInfoFromLoggedInUserAccessToken(AuditLog auditLog) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuthenticationToken){
            Map<String, Object> claims = jwtAuthenticationToken.getTokenAttributes();
            auditLog.setRole(claims.get("role").toString());
            auditLog.setName(claims.get("name").toString());
            auditLog.setEmail(claims.get("email").toString());

        }
    }

    public void saveAuditWithDescription(String role, String name, String email, String logActivityType, String description){
       AuditLog auditLog = new AuditLog();
       auditLog.setLogStartDate(LocalDateTime.now());
       auditLog.setRole(role);
       auditLog.setName(name);
       auditLog.setEmail(email);
       auditLog.setActivity(logActivityType);
       auditLog.setIpAddress(ClientUtil.getIpAddress(request));
       auditLog.setEmail(email);
       auditLog.setActivitySuccessfullyDone(true);
       auditLog.setActivityDescription(description);
       auditLog.setLogEndDate(LocalDateTime.now());
       auditLogRepository.save(auditLog);
   }

    public void saveAuditWithDescription(String role, String name, String email, String logActivityType, String auditType, String description){
        AuditLog auditLog = new AuditLog();
        auditLog.setLogStartDate(LocalDateTime.now());
        auditLog.setRole(role);
        auditLog.setName(name);
        auditLog.setEmail(email);
        auditLog.setActivity(logActivityType);
        auditLog.setAuditType(auditType);
        auditLog.setIpAddress(ClientUtil.getIpAddress(request));
        auditLog.setEmail(email);
        auditLog.setActivitySuccessfullyDone(true);
        auditLog.setActivityDescription(description);
        auditLog.setLogEndDate(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }

    public void saveAuditWithDescription(String logActivityType, String auditType, String description){
        AuditLog auditLog = new AuditLog();
        retrieveNecessaryInfoFromLoggedInUserAccessToken(auditLog);
        auditLog.setLogStartDate(LocalDateTime.now());
        auditLog.setActivity(logActivityType);
        auditLog.setAuditType(auditType);
        auditLog.setIpAddress(ClientUtil.getIpAddress(request));
        auditLog.setActivitySuccessfullyDone(true);
        auditLog.setActivityDescription(description);
        auditLog.setLogEndDate(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }

    public void saveAuditWithDescription(AuditLog auditLog, String logActivityType, String description){
        auditLog.setLogStartDate(LocalDateTime.now());
        auditLog.setRole("N/A");
        auditLog.setActivity(logActivityType);
        auditLog.setIpAddress(ClientUtil.getIpAddress(request));
        auditLog.setActivitySuccessfullyDone(true);
        auditLog.setActivityDescription(description);
        auditLog.setLogEndDate(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }




}
