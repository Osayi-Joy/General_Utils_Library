package com.osayijoy.common_utils_library.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osayijoy.common_utils_library.agent.config.LoggerConfig;
import com.osayijoy.common_utils_library.agent.config.MaskConfig;
import com.osayijoy.common_utils_library.agent.config.RedactionConfig;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;



@Component
@WebFilter
public class LoggingAgent implements Filter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${digicore.logger.enabled:false}")
    private boolean enableDigicoreLogger;

    @Value("${digicore.logger.config.location:file:}")
    private Resource loggerConfigFile;



    private List<String> uriBlacklist;
    private List<MaskConfig> maskConfigs;
    private List<RedactionConfig> redactionConfigs;
    private AntPathMatcher pathMatcher;

    @Override
    public void init(FilterConfig filterConfig) {
        if (enableDigicoreLogger) {
            logger.info("Digicore Logger - Initializing...");
            try {
                ObjectMapper mapper = new ObjectMapper();
                InputStream is = loggerConfigFile.getInputStream();
                LoggerConfig loggerConfig = mapper.readValue(is, LoggerConfig.class);

                uriBlacklist = loggerConfig.getUriBlacklist();
                maskConfigs = loggerConfig.getMaskConfigs();
                redactionConfigs = loggerConfig.getRedactionConfigs();

                pathMatcher = new AntPathMatcher();
                pathMatcher.setCaseSensitive(false);

                logger.info("Digicore Logger - Initialized");
            } catch (IOException ex) {
                enableDigicoreLogger = false;
                logger.warn("Digicore Logger - An error occurred while parsing configuration file... {}", ex.getMessage());
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // Check that Digicore Logger is enabled for app
        if (!enableDigicoreLogger) {
            // Not enabled... Move on
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletRequest request = ((HttpServletRequest) servletRequest);
            if (!(request instanceof ContentCachingRequestWrapper)) {
                request = new ContentCachingRequestWrapper(request);
            }

            HttpServletResponse response = ((HttpServletResponse) servletResponse);
            if (!(response instanceof ContentCachingResponseWrapper)) {
                response = new ContentCachingResponseWrapper(response);
            }

            // Check that the URI is not blacklisted
            uriBlacklist = uriBlacklist.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());

            String requestURI = request.getRequestURI();
            if (isBlacklistedPath(requestURI)) {
                // Blacklisted... Move on
                logger.info("{} blacklisted. Request info will not be logged.", requestURI);
                filterChain.doFilter(request, servletResponse);
            } else {
                try {
                    filterChain.doFilter(request, response);
                    } catch (Exception ex) {
                        logger.error("Unable to log request. {}", ex.getMessage());
                    }
                }
            }
        }

    @Override
    public void destroy() {
        // Not Implemented
    }

    private boolean isBlacklistedPath(String requestURI) {
        boolean matches = false;
        for (String uri : uriBlacklist) {
            if (pathMatcher.match(uri, requestURI)) {
                matches = true;
            }
        }
        return matches;
    }
}
