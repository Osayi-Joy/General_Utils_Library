package com.osayijoy.common_utils_library.soteria.repositories;


import java.util.List;
import java.util.Optional;

import com.osayijoy.common_utils_library.soteria.enums.ChannelMode;
import com.osayijoy.common_utils_library.soteria.models.Device;
import com.osayijoy.common_utils_library.soteria.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    boolean existsByUserAndIdentifierAndDeviceType(User user, String identifier, ChannelMode deviceType);

    boolean existsByUserAndIdentifierAndImeiAndDeviceType(User user, String identifier, String imei, ChannelMode deviceType);

    Optional<Device> findByIdentifierAndImeiAndDeviceType(String identifier, String imei, ChannelMode deviceType);
    Optional<Device> findByUserAndIdentifierAndImeiAndDeviceType(User user, String identifier, String imei, ChannelMode deviceType);

    List<Device> findAllByUser(User user);
    Optional<Device> findByUserAndIdentifier(User user,String identifier);

    Optional<Device> findByFingerprintCipherAndImei(String fingerprintCipher, String imei);

}
