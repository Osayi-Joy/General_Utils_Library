package com.osayijoy.common_utils_library.soteria.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.osayijoy.common_utils_library.commonUtils.util.BeanUtilWrapper;
import com.osayijoy.common_utils_library.commonUtils.util.ClientUtil;
import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.otp.enums.OtpType;
import com.osayijoy.common_utils_library.otp.service.OtpService;
import com.osayijoy.common_utils_library.soteria.config.SoteriaConfig;
import com.osayijoy.common_utils_library.soteria.dto.RegisterDeviceDTO;
import com.osayijoy.common_utils_library.soteria.dto.RoleDTO;
import com.osayijoy.common_utils_library.soteria.dto.UserDTO;
import com.osayijoy.common_utils_library.soteria.enums.ChannelMode;
import com.osayijoy.common_utils_library.soteria.models.Device;
import com.osayijoy.common_utils_library.soteria.models.User;
import com.osayijoy.common_utils_library.soteria.repositories.DeviceRepository;
import com.osayijoy.common_utils_library.soteria.services.implementations.UserDaoImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    private final UserDao userDao;

    private final UserDaoImpl userDaoImpl;

    private final SoteriaConfig soteriaConfig;

    private final OtpService otpService;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;



    //todo create class to hold new device registration info

    public void registerNewDeviceDuringOnboarding(RegisterDeviceDTO registerDeviceDTO, User user){
        if (!deviceRepository.existsByUserAndIdentifierAndImeiAndDeviceType(user, registerDeviceDTO.getIdentifier(), registerDeviceDTO.getImei(), registerDeviceDTO.getDeviceType())){
            Device device = new Device();
            BeanUtilWrapper.copyNonNullProperties(registerDeviceDTO, device);
            device.setDeviceType(registerDeviceDTO.getDeviceType());
            device.setUser(user);
            device.setFingerprintCipher(passwordEncoder.encode(registerDeviceDTO.getFingerprintCipher()));
            deviceRepository.save(device);
        } else {
            throw new ZeusRuntimeException("Device already registered");
        }
    }

    public boolean existsByUserAndIdentifierAndImeiAndDeviceType(User user, String identifier, String imei, ChannelMode channelMode){
        return deviceRepository.existsByUserAndIdentifierAndImeiAndDeviceType(user, identifier, imei, channelMode);
    }

    public UserDTO selectUserByFingerprintAndImei(String fingerprintCipher, String imei) {
        Device device = deviceRepository.findByFingerprintCipherAndImei(fingerprintCipher, imei).orElseThrow(() -> new BadCredentialsException("Fingerprints not valid"));

        User user = device.getUser();
        UserDTO userDTO = UserDTO.builder().build();
        BeanUtilWrapper.copyNonNullProperties(user,userDTO);
        RoleDTO roleDTO = userDaoImpl.buildRoleDTO(user);
        userDTO.setRoleDTO(roleDTO);
        userDTO.setName(user.getFirstName().concat(" ").concat(user.getLastName()));
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(device.getFingerprintCipher());
        userDTO.setDeviceName(device.getModel());
        return userDTO;
    }

    public UserDTO selectUserByIdentifierAndImeiAndChannelType(String identifier, String imei, ChannelMode channelMode) {
        Device device = deviceRepository.findByIdentifierAndImeiAndDeviceType(identifier, imei, channelMode).orElseThrow(() -> new BadCredentialsException("Fingerprints not valid"));

        User user = device.getUser();
        UserDTO userDTO = UserDTO.builder().build();
        BeanUtilWrapper.copyNonNullProperties(user,userDTO);
        RoleDTO roleDTO = userDaoImpl.buildRoleDTO(user);
        userDTO.setRoleDTO(roleDTO);
        userDTO.setName(user.getFirstName().concat(" ").concat(user.getLastName()));
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(device.getFingerprintCipher());
        userDTO.setDeviceName(device.getModel());
        return userDTO;
    }

    public UserDTO selectUserByIdentifierAndImeiAndChannelTypeAndRole(String identifier, String imei, ChannelMode channelMode, String role) {
        User user = userDao.getUserWithUsernameAndRole(identifier, role);
        Device device = deviceRepository.findByUserAndIdentifierAndImeiAndDeviceType(user, identifier, imei, channelMode).orElseThrow(() -> new BadCredentialsException("Fingerprints not valid"));
        UserDTO userDTO = UserDTO.builder().build();
        BeanUtilWrapper.copyNonNullProperties(user,userDTO);
        RoleDTO roleDTO = userDaoImpl.buildRoleDTO(user);
        userDTO.setRoleDTO(roleDTO);
        userDTO.setName(user.getFirstName().concat(" ").concat(user.getLastName()));
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(device.getFingerprintCipher());
        userDTO.setDeviceName(device.getModel());
        return userDTO;
    }

    public void registerNewDevice(String otp, RegisterDeviceDTO registerDeviceDTO){
        if(StringUtils.isBlank(otp)){
            throw new ZeusRuntimeException("OTP is required to add new device");
        }

        User user = userDao.getUser(ClientUtil.getLoggedInUsername());
            otpService.effect(user.getUsername(), OtpType.DEVICE_REGISTRATION, otp);

        Device device = new Device();

        if(deviceRepository.existsByUserAndIdentifierAndImeiAndDeviceType(user, user.getUsername(), registerDeviceDTO.getImei(), registerDeviceDTO.getDeviceType())) {
            if (soteriaConfig.isToAddDeviceOnLogin()) {
                BeanUtilWrapper.copyNonNullProperties(registerDeviceDTO, device);
                device.setUser(user);
                device.setLastLogin(LocalDateTime.now());
                device.setFingerprintCipher(passwordEncoder.encode(registerDeviceDTO.getFingerprintCipher()));
                deviceRepository.save(device);
                return;
            } else if (soteriaConfig.isToSwitchDeviceOnLogin()) {
                Optional<Device> deviceOptional = deviceRepository.findByUserAndIdentifier(user, registerDeviceDTO.getIdentifier());
                if (deviceOptional.isPresent()) {
                    device = deviceOptional.get();
                    BeanUtilWrapper.copyNonNullProperties(registerDeviceDTO, device);
                    device.setLastLogin(LocalDateTime.now());
                    device.setFingerprintCipher(passwordEncoder.encode(registerDeviceDTO.getFingerprintCipher()));
                    deviceRepository.save(device);
                } else {
                    device = new Device();
                    BeanUtilWrapper.copyNonNullProperties(registerDeviceDTO, device);
                    device.setUser(user);
                    device.setLastLogin(LocalDateTime.now());
                    device.setFingerprintCipher(passwordEncoder.encode(registerDeviceDTO.getFingerprintCipher()));
                    deviceRepository.save(device);
                }
                return;
            }

            throw new ZeusRuntimeException("Device is already linked to user account");
        } else {
            device = new Device();
            BeanUtilWrapper.copyNonNullProperties(registerDeviceDTO, device);
            device.setUser(user);
            device.setLastLogin(LocalDateTime.now());
            device.setFingerprintCipher(passwordEncoder.encode(registerDeviceDTO.getFingerprintCipher()));
            deviceRepository.save(device);
        }




    }

    public void addDeviceOnRegistration(User user, String identifier, ChannelMode deviceType){
        if(deviceRepository.existsByUserAndIdentifierAndDeviceType(user, identifier, deviceType)){
            return;
        }

        Device device = new Device();
        device.setUser(user);
        device.setIdentifier(identifier);
        device.setDeviceType(deviceType);

        deviceRepository.save(device);
    }

    public boolean isRegistered(String username, String identifier, ChannelMode deviceType){
        User user = userDao.getUser(username);
        return deviceRepository.existsByUserAndIdentifierAndDeviceType(user, identifier, deviceType);
    }

    public List<Device> getActiveDevices(String username){
        User user = userDao.getUser(username);
        return deviceRepository.findAllByUser(user);
    }
}
