package com.osayijoy.common_utils_library.soteria.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Component
public class SimpleCORSFilter implements WebMvcConfigurer {

    private final Logger log = LoggerFactory.getLogger(SimpleCORSFilter.class);

    @Autowired
    private SoteriaConfig propertyConfig;


    public SimpleCORSFilter() {
        log.info("SimpleCORSFilter init");
    }

    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST","PUT","DELETE","HEAD","PATCH");
    }

}

