package com.osayijoy.common_utils_library.soteria.config;

import com.osayijoy.common_utils_library.soteria.jwt.JwtHelper;
import com.osayijoy.common_utils_library.soteria.jwt.SoteriaAuthFilter;
import com.osayijoy.common_utils_library.soteria.services.DeviceService;
import com.osayijoy.common_utils_library.soteria.services.UserDao;
import com.osayijoy.common_utils_library.soteria.services.UserLoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;



@Configuration
@RequiredArgsConstructor
public class SoteriaBeanConfig {

    private final SoteriaConfig propertyConfig;

    private final UserDao userDao;

    private final DeviceService deviceService;

    private final UserLoginAttemptService userLoginAttemptService;

    private final PasswordEncoder passwordEncoder;

    private final JwtHelper jwtHelper;

    private final SoteriaApplicationSuccessConfig soteriaApplicationSuccessConfig;

    private final SoteriaApplicationErrorConfig soteriaApplicationErrorConfig;


    @Bean
    public FilterRegistrationBean<SoteriaAuthFilter> soteriaAuthFilterFilterRegistration(){
        FilterRegistrationBean<SoteriaAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SoteriaAuthFilter(propertyConfig, userDao, deviceService, userLoginAttemptService,passwordEncoder,jwtHelper,soteriaApplicationErrorConfig,soteriaApplicationSuccessConfig));
        registrationBean.addUrlPatterns("/login");
        registrationBean.setOrder(2);
        return registrationBean;
    }



}
