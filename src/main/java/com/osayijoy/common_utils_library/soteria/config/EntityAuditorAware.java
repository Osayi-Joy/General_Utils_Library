package com.osayijoy.common_utils_library.soteria.config;

import java.util.Optional;

import com.osayijoy.common_utils_library.commonUtils.util.ClientUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;



@Configuration
@RequiredArgsConstructor
public class EntityAuditorAware implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {
        String author = "SYSTEM";
        String loggedInUsername = ClientUtil.getLoggedInUsername();
        if (!StringUtils.isEmpty(loggedInUsername))
            author = loggedInUsername;

        return Optional.of(author);
    }
}
