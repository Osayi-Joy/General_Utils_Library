package com.osayijoy.common_utils_library.soteria.services.implementations;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import com.osayijoy.common_utils_library.soteria.models.Authority;
import com.osayijoy.common_utils_library.commonUtils.util.BeanUtilWrapper;
import com.osayijoy.common_utils_library.commonUtils.util.ClientUtil;
import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.otp.enums.OtpType;
import com.osayijoy.common_utils_library.otp.service.OtpService;
import com.osayijoy.common_utils_library.registhentication.registration.enums.Status;
import com.osayijoy.common_utils_library.soteria.config.SoteriaApplicationErrorConfig;
import com.osayijoy.common_utils_library.soteria.config.SoteriaConfig;
import com.osayijoy.common_utils_library.soteria.dto.*;
import com.osayijoy.common_utils_library.soteria.jwt.JwtHelper;
import com.osayijoy.common_utils_library.soteria.models.Role;
import com.osayijoy.common_utils_library.soteria.models.User;
import com.osayijoy.common_utils_library.soteria.repositories.UserRepository;
import com.osayijoy.common_utils_library.soteria.services.RoleService;
import com.osayijoy.common_utils_library.soteria.services.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
@Slf4j
public class UserDaoImpl implements UserDao {
    //todo use custom runtime exception

        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;

        private final RoleService roleService;

        private final SoteriaApplicationErrorConfig soteriaApplicationErrorConfig;

        private final SoteriaConfig soteriaConfig;
        private final OtpService otpService;

        private final JwtHelper jwtHelper;



    @Override
    public Role getUserRole(String username) {
        return userRepository.getRoleNameForUser(username);
    }

    @Override
    public UserDTO selectUserByUsername(String username) {
        User user = getUser(username);
        UserDTO userDTO = UserDTO.builder().build();
        BeanUtilWrapper.copyNonNullProperties(user,userDTO);
        RoleDTO roleDTO = buildRoleDTO(user);
        userDTO.setRoleDTO(roleDTO);
        userDTO.setName(user.getFirstName().concat(" ").concat(user.getLastName()));
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    @Override
    public UserDTO selectUserByUsernameAndRole(String username, String role) {
        User user = getUserWithUsernameAndRole(username, role);
        UserDTO userDTO = UserDTO.builder().build();
        BeanUtilWrapper.copyNonNullProperties(user,userDTO);
        RoleDTO roleDTO = buildRoleDTO(user);
        userDTO.setRoleDTO(roleDTO);
        userDTO.setName(user.getFirstName().concat(" ").concat(user.getLastName()));
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    @Override
    public void userNameExist(String username) {
        if (userRepository.existsByUsername(username))
            throw new ZeusRuntimeException(soteriaApplicationErrorConfig.getUserExistErrorMessage(),soteriaApplicationErrorConfig.getUserExistErrorCode(),HttpStatus.BAD_REQUEST);
    }

    @Override
    public void userNameExistsWithRole(String username, String role) {
        Role role1 = roleService.getRole(RoleDTO.builder().roleName(role).build());
        if (userRepository.existsByUsernameAndRoles(username, role1))
            throw new ZeusRuntimeException(soteriaApplicationErrorConfig.getUserExistErrorMessage(),soteriaApplicationErrorConfig.getUserExistErrorCode(),HttpStatus.BAD_REQUEST);
    }

    @Override
    public void emailExist(String email) {
        if (userRepository.existsByEmail(email))
            throw new ZeusRuntimeException(soteriaApplicationErrorConfig.getEmailExistErrorMessage(),soteriaApplicationErrorConfig.getEmailExistErrorCode(),HttpStatus.BAD_REQUEST);
    }

    @Override
    public void emailExistsWithRole(String email, String role) {
        Role role1 = roleService.getRole(RoleDTO.builder().roleName(role).build());
        if (userRepository.existsByEmailAndRoles(email, role1))
            throw new ZeusRuntimeException(soteriaApplicationErrorConfig.getEmailExistErrorMessage(),soteriaApplicationErrorConfig.getEmailExistErrorCode(),HttpStatus.BAD_REQUEST);
    }

    public RoleDTO buildRoleDTO(User user) {
        return RoleDTO.builder()
                .roleName(user.getRoles().getName())
                .permissions(user.getRoles().getPermissions().stream()
                        .map(authority -> AuthorityDTO.builder()
                                   .permissionDescription(authority.getDescription())
                                   .permissionType(authority.getPermissionType())
                                   .permission(authority.getPermission()).build()).toList())
                .build();
    }

    public User getUser (String username){
       return userRepository.findByUsername(username).orElseThrow(()->new ZeusRuntimeException(soteriaApplicationErrorConfig.getUserNotFoundErrorMessage(), soteriaApplicationErrorConfig.getUserNotFoundErrorCode(), HttpStatus.BAD_REQUEST));
    }

    @Override
    public User getUserWithUsernameAndRole(String username, String role) {
        Role role1 = roleService.getRole(RoleDTO.builder().roleName(role).build());
        return userRepository.findByUsernameAndRoles(username, role1).orElseThrow(()->new ZeusRuntimeException(soteriaApplicationErrorConfig.getUserNotFoundErrorMessage(), soteriaApplicationErrorConfig.getUserNotFoundErrorCode(), HttpStatus.BAD_REQUEST));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ZeusRuntimeException(soteriaApplicationErrorConfig.getUserNotFoundErrorMessage(), soteriaApplicationErrorConfig.getUserNotFoundErrorCode(), HttpStatus.BAD_REQUEST));
    }

    @Override
    public User getUserByEmailAndRole(String email, String role) {
        Role role1 = roleService.getRole(RoleDTO.builder().roleName(role).build());
        return userRepository.findByEmailAndRoles(email, role1).orElseThrow(() -> new ZeusRuntimeException(soteriaApplicationErrorConfig.getUserNotFoundErrorMessage(), soteriaApplicationErrorConfig.getUserNotFoundErrorCode(), HttpStatus.BAD_REQUEST));
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByEmail(phoneNumber).orElseThrow(() -> new ZeusRuntimeException(soteriaApplicationErrorConfig.getUserNotFoundErrorMessage(), soteriaApplicationErrorConfig.getUserNotFoundErrorCode(), HttpStatus.BAD_REQUEST));
    }

    @Override
    public User getUserByPhoneNumberAndRole(String phoneNumber, String role) {
        Role role1 = roleService.getRole(RoleDTO.builder().roleName(role).build());
        return userRepository.findByPhoneNumberAndRoles(phoneNumber, role1).orElseThrow(() -> new ZeusRuntimeException(soteriaApplicationErrorConfig.getUserNotFoundErrorMessage(), soteriaApplicationErrorConfig.getUserNotFoundErrorCode(), HttpStatus.BAD_REQUEST));
    }


    public boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("\\d+");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    @Override
    public User registerUser(RegisterUserRequestDTO registerUserRequest, Status status) {
        User user = new User();

        if(userRepository.existsByUsername(registerUserRequest.getUsername())){
            throw new ZeusRuntimeException(soteriaApplicationErrorConfig.getUserExistErrorMessage(),soteriaApplicationErrorConfig.getUserExistErrorMessage(),HttpStatus.BAD_REQUEST);
        }

        BeanUtilWrapper.copyNonNullProperties(registerUserRequest,user);
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setDefaultPassword(true);
        user.setRoles(roleService.getRole(registerUserRequest.getRoles()));
        user.setStatus(status);
        user = userRepository.save(user);

        String adminName = ClientUtil.getLoggedInUsername();
        RoleDTO roleDTO = RoleDTO.builder().roleName(soteriaConfig.getSystemAdminRoleName()).build();
        Role adminRole = roleService.getRole(roleDTO);
        if(adminName.equalsIgnoreCase(soteriaConfig.getSystemAdminRoleName()) && adminRole == roleService.getRole(registerUserRequest.getRoles()) ){
            User systemAdmin = getUser(adminName);
            systemAdmin.setStatus(Status.DEACTIVATED);
            userRepository.save(systemAdmin);
        }

        return user;
    }

    @Override
    public User registerUserWithRoleValidation(RegisterUserRequestDTO registerUserRequest, Status status) {
        User user = new User();

        Role role = roleService.getRole(registerUserRequest.getRoles());

        if(userRepository.existsByUsernameAndRoles(registerUserRequest.getUsername(), role)){
            throw new ZeusRuntimeException(soteriaApplicationErrorConfig.getUserExistErrorMessage(),soteriaApplicationErrorConfig.getUserExistErrorMessage(),HttpStatus.BAD_REQUEST);
        }

        BeanUtilWrapper.copyNonNullProperties(registerUserRequest,user);
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setDefaultPassword(true);
        user.setRoles(role);
        user.setStatus(status);
        user = userRepository.save(user);

        String adminName = ClientUtil.getLoggedInUsername();
        RoleDTO roleDTO = RoleDTO.builder().roleName(soteriaConfig.getSystemAdminRoleName()).build();
        Role adminRole = roleService.getRole(roleDTO);
        if(adminName.equalsIgnoreCase(soteriaConfig.getSystemAdminRoleName()) && adminRole == roleService.getRole(registerUserRequest.getRoles()) ){
            User systemAdmin = getUser(adminName);
            systemAdmin.setStatus(Status.DEACTIVATED);
            userRepository.save(systemAdmin);
        }

        return user;
    }

    public User verifyUser(RegisterUserRequestDTO registerUserRequestDTO) {
        this.otpService.effect(registerUserRequestDTO.getEmail().concat(registerUserRequestDTO.getPhoneNumber()), OtpType.CUSTOMER_REGISTRATION, registerUserRequestDTO.getRegistrationCode());
        return this.registerNewUser(registerUserRequestDTO, Status.ACTIVE);
    }

    @Override
    public User verifyUserWithRoleValidation(RegisterUserRequestDTO registerUserRequestDTO) {
        this.otpService.effect(registerUserRequestDTO.getEmail().concat(registerUserRequestDTO.getPhoneNumber()), OtpType.CUSTOMER_REGISTRATION, registerUserRequestDTO.getRegistrationCode());
        return this.registerNewUserWithRoleValidation(registerUserRequestDTO, Status.ACTIVE);
    }

    private User registerNewUser(RegisterUserRequestDTO registerUserRequest, Status status) {
        User user = new User();
        if (this.userRepository.existsByUsername(registerUserRequest.getUsername())) {
            throw new ZeusRuntimeException(this.soteriaApplicationErrorConfig.getUserExistErrorMessage(), this.soteriaApplicationErrorConfig.getUserExistErrorMessage(), HttpStatus.BAD_REQUEST);
        } else {
            BeanUtilWrapper.copyNonNullProperties(registerUserRequest, user);
            user.setPassword(this.passwordEncoder.encode(registerUserRequest.getPassword()));
            user.setRoles(roleService.getRole(registerUserRequest.getRoles()));
            user.setStatus(status);
            return userRepository.save(user);
        }
    }

    private User registerNewUserWithRoleValidation(RegisterUserRequestDTO registerUserRequest, Status status) {
        User user = new User();
        Role role = roleService.getRole(registerUserRequest.getRoles());
        if (this.userRepository.existsByUsernameAndRoles(registerUserRequest.getUsername(), role)) {
            throw new ZeusRuntimeException(this.soteriaApplicationErrorConfig.getUserExistErrorMessage(), this.soteriaApplicationErrorConfig.getUserExistErrorMessage(), HttpStatus.BAD_REQUEST);
        } else {
            BeanUtilWrapper.copyNonNullProperties(registerUserRequest, user);
            user.setPassword(this.passwordEncoder.encode(registerUserRequest.getPassword()));
            user.setRoles(roleService.getRole(registerUserRequest.getRoles()));
            user.setStatus(status);
            return userRepository.save(user);
        }
    }

    @Override
    public User verifyUserMobile(VerifyUserDTO verifyUserDTO) {
        otpService.effect(verifyUserDTO.getPhoneNumber(), OtpType.CUSTOMER_REGISTRATION_MOBILE, verifyUserDTO.getOtp());
        User user = getUserByPhoneNumber(verifyUserDTO.getPhoneNumber());
        user.setStatus(Status.ACTIVE);
        return userRepository.save(user);
    }

    @Override
    public void updateUserPassword(RecoveryPasswordDTO recoveryPasswordDTO, boolean isForReset) {
        User user = getUser(ClientUtil.getLoggedInUsername().isEmpty()?recoveryPasswordDTO.getUsername():ClientUtil.getLoggedInUsername());

        if (recoveryPasswordDTO.getOtp() != null){
            otpService.effect(recoveryPasswordDTO.getUsername(), OtpType.PASSWORD_UPDATE, recoveryPasswordDTO.getOtp());
        }

        if (!isForReset && !passwordEncoder.matches(recoveryPasswordDTO.getOldPassword(), user.getPassword())) {
            throw new ZeusRuntimeException(soteriaApplicationErrorConfig.getPasswordMisMatchErrorMessage(), soteriaApplicationErrorConfig.getPasswordMisMatchErrorCode(), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(recoveryPasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void resetPasswordWithOTP(RecoveryPasswordDTO recoveryPasswordDTO, boolean isForReset) {
        otpService.effect(recoveryPasswordDTO.getUsername(), OtpType.PASSWORD_UPDATE, recoveryPasswordDTO.getOtp());
        User user = getUser(recoveryPasswordDTO.getUsername());
        user.setPassword(passwordEncoder.encode(recoveryPasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUserPassword(RecoveryPasswordDTO recoveryPasswordDTO, String role, boolean isForReset) {
        User user = getUserWithUsernameAndRole(ClientUtil.getLoggedInUsername().isEmpty()?recoveryPasswordDTO.getUsername():ClientUtil.getLoggedInUsername(), role);

        if (recoveryPasswordDTO.getOtp() != null){
            otpService.effect(recoveryPasswordDTO.getUsername(), OtpType.PASSWORD_UPDATE, recoveryPasswordDTO.getOtp());
        }

        if (!isForReset && !passwordEncoder.matches(recoveryPasswordDTO.getOldPassword(), user.getPassword())) {
            throw new ZeusRuntimeException(soteriaApplicationErrorConfig.getPasswordMisMatchErrorMessage(), soteriaApplicationErrorConfig.getPasswordMisMatchErrorCode(), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(recoveryPasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void resetPasswordWithOTP(RecoveryPasswordDTO recoveryPasswordDTO, String role, boolean isForReset) {
        otpService.effect(recoveryPasswordDTO.getUsername(), OtpType.PASSWORD_UPDATE, recoveryPasswordDTO.getOtp());
        User user = getUserWithUsernameAndRole(recoveryPasswordDTO.getUsername(), role);
        user.setPassword(passwordEncoder.encode(recoveryPasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public String updateDefaultPassword(RecoveryPasswordDTO recoveryPasswordDTO) {
        User user = getUser(ClientUtil.getLoggedInUsername());
        user.setDefaultPassword(false);
        user.setPassword(passwordEncoder.encode(recoveryPasswordDTO.getNewPassword()));
        userRepository.save(user);
        Map<String, String> claims = new HashMap<>();
        claims.put("authorities", user.getRoles().getPermissions().stream().map(Authority::getPermission).toList().toString());
        claims.put("role", user.getRoles().getName());
        return jwtHelper.createJwtForClaims(user.getUsername(),claims);
    }


    @Override
    public void updateUserPin(User user, String newPin) {
        user.setPin(passwordEncoder.encode(newPin));
        userRepository.save(user);
    }

    @Override
    public void updateUserFingerPrint(User user, String fingerPrintCipher, String updatedFingerPrintCipher, boolean isForReset) {

    }

    public void lockAccount(User user, String reason) {
        user.setStatus(Status.LOCKED);
        userRepository.save(user);

        auditLockAction(user, reason);
    }

    private void auditLockAction(User user, String reason) {
    }

    @Override
    public void resetWrongPasswordTries(User user) {
        if(user.getWrongPasswordTries() > 0){
            user.setWrongPasswordTries(0);
            userRepository.save(user);
        }
    }

    @Override
    public void resetWrongPinTries(User user) {
        if(user.getWrongPinTries() > 0){
            user.setWrongPinTries(0);
            userRepository.save(user);
        }
    }

    public void handleSoteriaUserIncorrectPin(User user){
        long maxAttempt = 4;
        if(user.getWrongPinTries() == maxAttempt - 1){
            lockAccount(user, "Maxed incorrect Pin Usage");
            throw new ZeusRuntimeException(soteriaApplicationErrorConfig.getAccountDisabledErrorMessage(),soteriaApplicationErrorConfig.getAccountDisabledErrorCode(),HttpStatus.FORBIDDEN);
        }
        user.setWrongPinTries(user.getWrongPinTries() + 1);
        userRepository.save(user);
        throw new ZeusRuntimeException(String.format("Invalid PIN, You have %d more incorrect attempts before your account is locked.", maxAttempt - user.getWrongPinTries()),HttpStatus.BAD_REQUEST);
    }

    public String handleSoteriaUserIncorrectPassWord(String username){
        long maxAttempt = 4;

        User user = getUser(username);

        if(user.getWrongPasswordTries() == maxAttempt - 1){
            lockAccount(user, "Maxed incorrect Password Usage");
            throw new ZeusRuntimeException(soteriaApplicationErrorConfig.getAccountDisabledErrorMessage(),soteriaApplicationErrorConfig.getAccountDisabledErrorCode(),HttpStatus.FORBIDDEN);
        }

        user.setWrongPasswordTries(user.getWrongPasswordTries() + 1);
        userRepository.save(user);

        return String.format("Wrong password, You have %d more incorrect attempts before your account is disabled.", maxAttempt - user.getWrongPasswordTries());
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }


}
