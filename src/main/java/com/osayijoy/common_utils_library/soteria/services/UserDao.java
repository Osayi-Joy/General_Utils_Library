package com.osayijoy.common_utils_library.soteria.services;


import com.osayijoy.common_utils_library.registhentication.registration.enums.Status;
import com.osayijoy.common_utils_library.soteria.dto.RecoveryPasswordDTO;
import com.osayijoy.common_utils_library.soteria.dto.RegisterUserRequestDTO;
import com.osayijoy.common_utils_library.soteria.dto.UserDTO;
import com.osayijoy.common_utils_library.soteria.dto.VerifyUserDTO;
import com.osayijoy.common_utils_library.soteria.models.Role;
import com.osayijoy.common_utils_library.soteria.models.User;

public interface UserDao {

    UserDTO selectUserByUsername(String username);

    UserDTO selectUserByUsernameAndRole(String username, String role);

    void userNameExist(String username);

    void userNameExistsWithRole(String username, String role);

    void emailExist(String email);

    void emailExistsWithRole(String email, String role);
    User getUser(String username);

    User getUserWithUsernameAndRole(String username, String role);

    User getUserByEmail(String email);

    User getUserByEmailAndRole(String email, String role);

    User getUserByPhoneNumber(String phoneNumber);

    User getUserByPhoneNumberAndRole(String phoneNumber, String role);

    User registerUser(RegisterUserRequestDTO registerUserRequest, Status status);

    User registerUserWithRoleValidation(RegisterUserRequestDTO registerUserRequest, Status status);

    User verifyUser(RegisterUserRequestDTO registerUserRequestDTO);

    User verifyUserWithRoleValidation(RegisterUserRequestDTO registerUserRequestDTO);

    User verifyUserMobile(VerifyUserDTO verifyUserDTO);

    void updateUserPassword(RecoveryPasswordDTO recoveryPasswordDTO, boolean isForReset);

    void resetPasswordWithOTP(RecoveryPasswordDTO recoveryPasswordDTO, boolean isForReset);

    void updateUserPassword(RecoveryPasswordDTO recoveryPasswordDTO, String role, boolean isForReset);

    void resetPasswordWithOTP(RecoveryPasswordDTO recoveryPasswordDTO, String role, boolean isForReset);

    String updateDefaultPassword(RecoveryPasswordDTO recoveryPasswordDTO);

    void updateUserPin(User user, String newPin);

    void updateUserFingerPrint(User user, String fingerPrintCipher, String updatedFingerPrintCipher, boolean isForReset);


    void lockAccount(User user, String reason);

    void resetWrongPasswordTries(User user);

    void resetWrongPinTries(User user);

    void handleSoteriaUserIncorrectPin(User user);

    String handleSoteriaUserIncorrectPassWord(String username);

    void updateUser(User user);

    boolean isNumeric(String strNum);
    Role getUserRole(String username);

}

