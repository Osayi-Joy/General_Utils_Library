package com.osayijoy.common_utils_library.processor.processors;

import java.util.Optional;

import com.osayijoy.common_utils_library.commonUtils.util.ClientUtil;
import com.osayijoy.common_utils_library.processor.model.TokenVault;
import com.osayijoy.common_utils_library.processor.repository.TokenVaultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;



@ConditionalOnProperty(name = "token.validation", havingValue = "true")
@EntityScan("com.digicore.request.processor.model")
@EnableJpaRepositories(basePackages = {"com.digicore.request.processor.token_repository"})
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class TokenVaultProcessor {

    private final TokenVaultRepository tokenVaultRepository;

    @Around("@annotation(com.osayijoy.common_utils_library.processor.annotations.TokenValid)")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken){
            Optional<TokenVault> tokenVault = tokenVaultRepository.findByUserName(ClientUtil.getLoggedInUsername());
            if (tokenVault.isEmpty())
                throw new BadCredentialsException("invalid token");
            if (!jwtAuthenticationToken.getToken().getClaims().get("client_mapper").toString().equalsIgnoreCase(tokenVault.get().getToken()))
                throw new BadCredentialsException("invalid token");
            else return joinPoint.proceed();
        } else throw new BadCredentialsException("invalid token");

    }

    public void saveUserToTokenVault(String username, String token){
        if (tokenVaultRepository.findByUserName(username).isPresent())
            removeUserFromTokenVault(username);
        TokenVault tokenVault = new TokenVault();
        tokenVault.setUserName(username);
        tokenVault.setToken(token);
        tokenVaultRepository.save(tokenVault);
    }

    public void removeUserFromTokenVault(String username){
        TokenVault tokenVault = tokenVaultRepository.findByUserName(username).orElseThrow();
        tokenVaultRepository.delete(tokenVault);
    }

    public boolean confirmToken(String token){
        return  tokenVaultRepository.findByToken(token).isPresent();
    }
}
