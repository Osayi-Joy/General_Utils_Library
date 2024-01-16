package com.osayijoy.common_utils_library.soteria.services;


import java.util.Objects;
import java.util.Optional;

import com.osayijoy.common_utils_library.registhentication.registration.enums.Status;
import com.osayijoy.common_utils_library.soteria.constants.ApplicationPropertyConfigKeys;
import com.osayijoy.common_utils_library.soteria.models.User;
import com.osayijoy.common_utils_library.soteria.models.UserLoginAttempt;
import com.osayijoy.common_utils_library.soteria.repositories.UserLoginAttemptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserLoginAttemptService {

    private final UserLoginAttemptRepository repository;

    private final SystemConfigurationService systemConfigurationService;

    public Optional<UserLoginAttempt> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public UserLoginAttempt save(UserLoginAttempt userLoginAttempt) {
        return repository.save(userLoginAttempt);
    }

    public UserLoginAttempt systemLockUser(UserLoginAttempt userLoginAttempt) {
        userLoginAttempt.setLocked(true);
        return repository.save(userLoginAttempt);
    }

    public UserLoginAttempt getOrCreateByUsername(String username) {
        return findByUsername(username).orElseGet(() -> {
            UserLoginAttempt loginAttempt = new UserLoginAttempt();
            loginAttempt.setFailedAttempts(0);
            loginAttempt.setLocked(false);
            loginAttempt.setUsername(username);
            loginAttempt.setStatus(Status.ACTIVE);
            return save(loginAttempt);
        });
    }

    public int getMaxAttempts() {
        Integer maxLoginAttempts = systemConfigurationService.getInteger(ApplicationPropertyConfigKeys.MAX_LOGIN_ATTEMPTS);
        if (Objects.isNull(maxLoginAttempts)) {
            maxLoginAttempts = 5;
        }
        return maxLoginAttempts;
    }

    public UserLoginAttempt unlockUser(User user) {
        UserLoginAttempt userLoginAttempt = getOrCreateByUsername(user.getUsername());
        userLoginAttempt.setLocked(false);
        userLoginAttempt.setFailedAttempts(0);
        repository.save(userLoginAttempt);
        return userLoginAttempt;
    }

}
