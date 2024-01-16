package com.osayijoy.common_utils_library.soteria.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;
import com.osayijoy.common_utils_library.helper.response.ApiResponseJson;
import com.osayijoy.common_utils_library.registhentication.registration.enums.Status;
import com.osayijoy.common_utils_library.soteria.api_models.request.UsernameAndPasswordAuthenticationRequest;
import com.osayijoy.common_utils_library.soteria.api_models.response.LoginApiModel;
import com.osayijoy.common_utils_library.soteria.config.SoteriaApplicationErrorConfig;
import com.osayijoy.common_utils_library.soteria.config.SoteriaApplicationSuccessConfig;
import com.osayijoy.common_utils_library.soteria.config.SoteriaConfig;
import com.osayijoy.common_utils_library.soteria.dto.UserDTO;
import com.osayijoy.common_utils_library.soteria.enums.AuthenticationType;
import com.osayijoy.common_utils_library.soteria.enums.ChannelMode;
import com.osayijoy.common_utils_library.soteria.enums.DeviceStatus;
import com.osayijoy.common_utils_library.soteria.models.User;
import com.osayijoy.common_utils_library.soteria.models.UserLoginAttempt;
import com.osayijoy.common_utils_library.soteria.services.DeviceService;
import com.osayijoy.common_utils_library.soteria.services.UserDao;
import com.osayijoy.common_utils_library.soteria.services.UserLoginAttemptService;
import com.osayijoy.common_utils_library.soteria.dto.AuthorityDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SoteriaAuthFilter implements Filter {

    private final SoteriaConfig propertyConfig;

    private final UserDao userDao;

    private final DeviceService deviceService;
    private final UserLoginAttemptService userLoginAttemptService;


    private final PasswordEncoder passwordEncoder;

    private final JwtHelper jwtHelper;

    private final SoteriaApplicationErrorConfig soteriaApplicationErrorConfig;

    private final SoteriaApplicationSuccessConfig soteriaApplicationSuccessConfig;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        UsernameAndPasswordAuthenticationRequest authenticationRequest;
        try {
            authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
        } catch (Exception e) {
            writeResponseToTheServlet(response, getFailedAuthenticationResponse(soteriaApplicationErrorConfig.getBadRequestErrorCode(), soteriaApplicationErrorConfig.getBadRequestErrorMessage(), response));
            return;
        }

        UserLoginAttempt userLoginAttempt = getUserLoginAttempt(authenticationRequest);

        UserDTO user;

        if (authenticationRequest.getAuthenticationType() == AuthenticationType.PASSWORD){
            if (validateLoginAttempt(response, userLoginAttempt))
                return;

            if (authenticationRequest.getRole() != null){
                user = retrieveUserFromDBAlt(authenticationRequest.getUsername(), authenticationRequest.getRole(), response, userLoginAttempt, authenticationRequest.getNotificationToken());
            } else {
                user = retrieveUserFromDB(authenticationRequest.getUsername(), response, userLoginAttempt, authenticationRequest.getNotificationToken());
            }

            if (user == null)
                return;
            if(!validateAccountStatus(user,response, userLoginAttempt))
                return;
            authenticate(authenticationRequest,user,request,response,userLoginAttempt,filterChain);
        }

        if (authenticationRequest.getAuthenticationType() == AuthenticationType.FINGERPRINT){
            if (authenticationRequest.getRole() != null){
                user = retrieveUserByIdentifierAndImeiAndDeviceType(authenticationRequest.getUsername(), authenticationRequest.getImei(), authenticationRequest.getChannelMode(), response, authenticationRequest.getNotificationToken(), authenticationRequest.getRole());
            } else {
                user = retrieveUserByIdentifierAndImeiAndDeviceType(authenticationRequest.getUsername(), authenticationRequest.getImei(), authenticationRequest.getChannelMode(), response, authenticationRequest.getNotificationToken());

            }
            if (user == null)
                return;
            if(!validateAccountStatus(user,response, userLoginAttempt))
                return;
            authenticate(authenticationRequest,user,request,response,userLoginAttempt,filterChain);
        }


    }

    @SneakyThrows
    private void authenticate(UsernameAndPasswordAuthenticationRequest request, UserDTO userDTO, ServletRequest servletRequest, ServletResponse response, UserLoginAttempt userLoginAttempt, FilterChain filterChain) {
        boolean isAuthenticated = false;
        String token = null;
        try {
            switch (request.getAuthenticationType()) {
                case PIN -> isAuthenticated = passwordEncoder.matches(request.getPin(), userDTO.getPin());
                case OTP ->{
                    //todo validate otp isAuthenticated = false;
                }
                case PASSWORD -> isAuthenticated = passwordEncoder.matches(request.getPassword(), userDTO.getPassword());
                case FINGERPRINT -> isAuthenticated = passwordEncoder.matches(request.getFingerPrintCipher(), userDTO.getPassword());

                default -> isAuthenticated = passwordEncoder.matches(request.getPassword(), userDTO.getPassword());

            }
            
            if (isAuthenticated) {
                token = getAccessToken(userLoginAttempt, userDTO);

                HttpServletResponse servletResponse = prepareSuccessResponseBody(response, request, token,userDTO);
                filterChain.doFilter(servletRequest, servletResponse);

            } else
                writeResponseToTheServlet(response, getFailedAuthenticationResponse(soteriaApplicationErrorConfig.getBadCredentialErrorCode(), soteriaApplicationErrorConfig.getBadCredentialErrorMessage(), response, userLoginAttempt));
        } catch (Exception e) {
            writeResponseToTheServlet(response, getFailedAuthenticationResponse(soteriaApplicationErrorConfig.getBadCredentialErrorCode(), soteriaApplicationErrorConfig.getBadCredentialErrorMessage(), response, userLoginAttempt));
        }

    }

    @SneakyThrows
    private boolean validateLoginAttempt(ServletResponse response, UserLoginAttempt userLoginAttempt) {
        if (userLoginAttempt.isLocked()) {
            writeResponseToTheServlet(response, getFailedAuthenticationResponse(soteriaApplicationErrorConfig.getAccountLockedErrorCode(), soteriaApplicationErrorConfig.getAccountLockedErrorMessage(), response, userLoginAttempt));
            return true;
        }
        return false;
    }

    private UserLoginAttempt getUserLoginAttempt(UsernameAndPasswordAuthenticationRequest authenticationRequest) {
        return userLoginAttemptService.getOrCreateByUsername(authenticationRequest.getUsername());
    }

    private static void writeResponseToTheServlet(ServletResponse response, ApiResponseJson<Object> baseResponse) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(new Gson().toJson(baseResponse));
        out.flush();
    }

    private ApiResponseJson<Object> getFailedAuthenticationResponse(String code, String message, ServletResponse response, UserLoginAttempt userLoginAttempt) {
        userLoginAttempt.setFailedAttempts(userLoginAttempt.getFailedAttempts() + 1);
        if (userLoginAttempt.getFailedAttempts() >= userLoginAttemptService.getMaxAttempts()) {
            userLoginAttemptService.systemLockUser(userLoginAttempt);
        }
        userLoginAttemptService.save(userLoginAttempt);
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ApiResponseJson.builder()
                .success(false)
                .message(soteriaApplicationErrorConfig.getLoginFailedErrorMessage())
                .errors(List.of(new ApiError(message, code)))
                .build();
    }

    private ApiResponseJson<Object> getFailedAuthenticationResponse(String code, String message, ServletResponse response) {

        HttpServletResponse servletResponse = (HttpServletResponse) response;

        servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return ApiResponseJson.builder()
                .success(false)
                .message(soteriaApplicationErrorConfig.getLoginFailedErrorMessage())
                .errors(List.of(new ApiError(message, code)))
                .build();
    }

    @SneakyThrows
    private UserDTO retrieveUserFromDB(String username, ServletResponse response, UserLoginAttempt userLoginAttempt, String notificationToken) {
        try {
            UserDTO userDTO = userDao.selectUserByUsername(username);
            userDTO.setNotificationToken(notificationToken);
            return userDTO;
        } catch (ZeusRuntimeException e) {
            writeResponseToTheServlet(response, getFailedAuthenticationResponse(e.getCode(), e.getMessage(), response, userLoginAttempt));
        }
        return null;
    }

    @SneakyThrows
    private UserDTO retrieveUserFromDBAlt(String username, String role, ServletResponse response, UserLoginAttempt userLoginAttempt, String notificationToken) {
        try {
            UserDTO userDTO = userDao.selectUserByUsernameAndRole(username, role);
            userDTO.setNotificationToken(notificationToken);
            return userDTO;
        } catch (ZeusRuntimeException e) {
            writeResponseToTheServlet(response, getFailedAuthenticationResponse(e.getCode(), e.getMessage(), response, userLoginAttempt));
        }
        return null;
    }

    @SneakyThrows
    private UserDTO retrieveUserByIdentifierAndImeiAndDeviceType(String identifier, String imei, ChannelMode channelMode, ServletResponse response, String notificationToken) {
        try {
            UserDTO userDTO = deviceService.selectUserByIdentifierAndImeiAndChannelType(identifier, imei, channelMode);
            userDTO.setNotificationToken(notificationToken);
            return userDTO;
        } catch (ZeusRuntimeException e) {
            writeResponseToTheServlet(response, ApiResponseJson.builder().message(e.getMessage()).build());
        }
        return null;
    }

    @SneakyThrows
    private UserDTO retrieveUserByIdentifierAndImeiAndDeviceType(String identifier, String imei, ChannelMode channelMode, ServletResponse response, String notificationToken, String role) {
        try {
            UserDTO userDTO = deviceService.selectUserByIdentifierAndImeiAndChannelTypeAndRole(identifier, imei, channelMode, role);
            userDTO.setNotificationToken(notificationToken);
            return userDTO;
        } catch (ZeusRuntimeException e) {
            writeResponseToTheServlet(response, ApiResponseJson.builder().message(e.getMessage()).build());
        }
        return null;
    }

    @SneakyThrows
    private boolean validateAccountStatus(UserDTO user, ServletResponse response, UserLoginAttempt userLoginAttempt) {

        if (user.getStatus().equals(Status.LOCKED)) {
            writeResponseToTheServlet(response, getFailedAuthenticationResponse(soteriaApplicationErrorConfig.getAccountDisabledErrorCode(), soteriaApplicationErrorConfig.getAccountDisabledErrorMessage(), response, userLoginAttempt));
            return false;
        }

        if (user.getStatus().equals(Status.PENDING_INVITE_ACCEPTANCE)) {
            return true;
        }

        if (!user.getStatus().equals(Status.ACTIVE)) {
            writeResponseToTheServlet(response, getFailedAuthenticationResponse(soteriaApplicationErrorConfig.getAccountDeactivatedErrorCode(), soteriaApplicationErrorConfig.getAccountDisabledErrorMessage(), response, userLoginAttempt));
            return false;
        }

        return true;

    }

    @SneakyThrows
    private HttpServletResponse prepareSuccessResponseBody(ServletResponse response, UsernameAndPasswordAuthenticationRequest authenticationRequest, String token, UserDTO userDTO) {
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        servletResponse.setStatus(HttpServletResponse.SC_OK);
        ApiResponseJson<Object> apiModelApiResponseJson = ApiResponseJson.builder()
                .success(true)
                .message(soteriaApplicationSuccessConfig.getLoginSuccessMessage())
                .errors(new ArrayList<>())
                .build();
        PrintWriter out = response.getWriter();
        boolean registered = false;
        DeviceStatus deviceStatus = getDeviceStatus(authenticationRequest, registered);
        HashMap<String, Object> additionalInformation = new HashMap<>();
        additionalInformation.put("email",userDTO.getEmail());
        additionalInformation.put("isDefault",userDTO.isDefaultPassword());
        additionalInformation.put("notificationToken", userDTO.getNotificationToken());
        additionalInformation.put("deviceName", userDTO.getDeviceName());
        LoginApiModel loginApiModel = LoginApiModel.builder()
                .token(token)
                .deviceStatus(deviceStatus)
                .additionalInformation(additionalInformation)
                .build();
        apiModelApiResponseJson.setData(loginApiModel);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(new Gson().toJson(apiModelApiResponseJson));
        out.flush();
        return servletResponse;
    }

    private String getAccessToken(UserLoginAttempt userLoginAttempt, UserDTO user) {
        userLoginAttempt.setFailedAttempts(0);
        userLoginAttemptService.save(userLoginAttempt);


        Map<String, String> claims = new HashMap<>();
        claims.put("authorities", user.getRoleDTO().getPermissions().stream().map(AuthorityDTO::getPermission).toList().toString());
        claims.put("role", user.getRoleDTO().getRoleName());

        return jwtHelper.createJwtForClaims(user.getUsername(), claims);
    }

    private DeviceStatus getDeviceStatus(UsernameAndPasswordAuthenticationRequest authenticationRequest, boolean registered) {
        DeviceStatus deviceStatus;
        User user;
        if (authenticationRequest.getRole() != null){
            user = userDao.getUserWithUsernameAndRole(authenticationRequest.getUsername(), authenticationRequest.getRole());
        } else {
            user = userDao.getUser(authenticationRequest.getUsername());
        }
        if (propertyConfig.isToRegisterDeviceOnLogin()) {
            registered = deviceService.isRegistered(authenticationRequest.getUsername(), authenticationRequest.getUsername(), authenticationRequest.getChannelMode());
            if (registered)
                deviceStatus = DeviceStatus.REGISTERED_DEVICE;
            else
                deviceStatus = DeviceStatus.NOT_REGISTERED_DEVICE;
        } else {
            deviceStatus = DeviceStatus.UNKNOWN_DEVICE;
        }
        if (propertyConfig.isToAddDeviceOnLogin() && !registered) {
            deviceStatus = DeviceStatus.ADD_DEVICE;
        } else if (propertyConfig.isToSwitchDeviceOnLogin() && !registered) {
            deviceStatus = DeviceStatus.SWITCH_DEVICE;
        }

        if (deviceService.existsByUserAndIdentifierAndImeiAndDeviceType(user, authenticationRequest.getUsername(), authenticationRequest.getImei(), authenticationRequest.getChannelMode())){
            deviceStatus = DeviceStatus.REGISTERED_DEVICE;
        }
        return deviceStatus;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }


}
