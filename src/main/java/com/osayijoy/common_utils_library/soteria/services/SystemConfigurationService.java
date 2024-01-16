package com.osayijoy.common_utils_library.soteria.services;


import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.github.drapostolos.typeparser.TypeParser;
import com.osayijoy.common_utils_library.commonUtils.util.BeanUtilWrapper;
import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.soteria.api_models.request.AddSystemConfigurationRequest;
import com.osayijoy.common_utils_library.soteria.config.SoteriaApplicationErrorConfig;
import com.osayijoy.common_utils_library.soteria.enums.SystemConfigurationGroup;
import com.osayijoy.common_utils_library.soteria.models.SystemConfiguration;
import com.osayijoy.common_utils_library.soteria.repositories.SystemConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemConfigurationService {

    private final SystemConfigurationRepository systemConfigurationRepository;

    private final SoteriaApplicationErrorConfig soteriaApplicationErrorConfig;

    private List<SystemConfiguration> cachedSystemConfiguration = new ArrayList<>();

    private final TypeParser typeParser = TypeParser.newBuilder().build();

    public String getString(String key) {
        return this.getConfig(key, String.class);
    }

    public Boolean getBoolean(String key) {
        return this.getConfig(key, Boolean.class);
    }

    public Integer getInteger(String key) {
        return this.getConfig(key, Integer.class);
    }

    public Long getLong(String key) {
        return this.getConfig(key, Long.class);
    }

    public Double getDouble(String key) {
        return this.getConfig(key, Double.class);
    }

    public BigDecimal getBigDecimal(String key) {
        return this.getConfig(key, BigDecimal.class);
    }

    public Date getDate(String key) {
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(this.getConfig(key, String.class));
        } catch (ParseException e) {
            throw new ZeusRuntimeException(String.format("Unable to parse config value with key - %s to date", key));
        }
    }

    public List<?> getList(String key) {
        return this.getConfig(key, List.class);
    }

    public <T> T getEnum(String key, Class<T> enumType) {
        String value = this.getConfig(key, String.class);
        if (enumType.isEnum()) {
            for (T t : enumType.getEnumConstants()) {
                if (((Enum<?>) t).name().equals(value)) {
                    return t;
                }
            }
        }
        throw new ZeusRuntimeException(String.format("No Valid ENUM for config value with key - %s", key));
    }

    public <T> T getConfig(String key, Class<T> clazz) {
        if (cachedSystemConfiguration.isEmpty()) {
            reloadSystemConfigurations();
        }

        Optional<SystemConfiguration> systemConfigurationOptional = cachedSystemConfiguration
                .stream()
                .filter(sc -> sc.getKey().equals(key))
                .findFirst(); //Compound unique constraint on column KEY/GROUP guarantees this to be distinct



        String dbValue = systemConfigurationOptional.get().getValue();

        //Treat Date separately
        if (clazz == Date.class) {
            return (T) dbValue;
        }

        //Can handle all other types
        return (T) typeParser.parseType(dbValue, clazz);
    }



    public void reloadSystemConfigurations() {
        cachedSystemConfiguration = systemConfigurationRepository.findAll();
    }



    public SystemConfiguration findById(Long id) {
        Optional<SystemConfiguration> systemConfigurationOptional = systemConfigurationRepository.findById(id);

        if (systemConfigurationOptional.isPresent()) {
            return systemConfigurationOptional.get();
        }

        throw new ZeusRuntimeException("System Configuration not found", HttpStatus.BAD_REQUEST);
    }

    private void validateConfig(String key, SystemConfigurationGroup group) {
        if (systemConfigurationRepository.findByKeyAndGroup(key, group).isPresent()) {
            throw new ZeusRuntimeException(String.format("Config key %s already configured for group %s", key, group),HttpStatus.BAD_REQUEST);
        }
    }
    
    public SystemConfiguration addConfiguration(AddSystemConfigurationRequest addSystemConfigurationRequest) {
       validateConfig(addSystemConfigurationRequest.getValue(), addSystemConfigurationRequest.getGroup());

        SystemConfiguration systemConfiguration = new SystemConfiguration();

        BeanUtilWrapper.copyNonNullProperties(addSystemConfigurationRequest,systemConfiguration);

        systemConfiguration.setKey(addSystemConfigurationRequest.getValue());
        return systemConfigurationRepository.save(systemConfiguration);
    }

    public void validateAgentSystemConfigurationFields(String key, SystemConfigurationGroup systemConfigurationGroup) {

        if (!this.systemConfigurationRepository.existsByKeyAndGroup(key, systemConfigurationGroup) && systemConfigurationGroup == SystemConfigurationGroup.AGENT_STATUS) {
            throw new ZeusRuntimeException(this.soteriaApplicationErrorConfig.getInvalidAgentStatusErrorMessage(), this.soteriaApplicationErrorConfig.getInvalidAgentSystemConfigurationErrorCode(), HttpStatus.BAD_REQUEST);
        }

        if (!this.systemConfigurationRepository.existsByKeyAndGroup(key, systemConfigurationGroup) && systemConfigurationGroup == SystemConfigurationGroup.AGENT_TYPE) {
            throw new ZeusRuntimeException(this.soteriaApplicationErrorConfig.getInvalidAgentTypeErrorMessage(), this.soteriaApplicationErrorConfig.getInvalidAgentSystemConfigurationErrorCode(), HttpStatus.BAD_REQUEST);
        }

        if (!this.systemConfigurationRepository.existsByKeyAndGroup(key, systemConfigurationGroup) && systemConfigurationGroup == SystemConfigurationGroup.RECRUITMENT_SOURCES) {
            throw new ZeusRuntimeException(this.soteriaApplicationErrorConfig.getAgentSourceOfRecruitmentTypeErrorMessage(), this.soteriaApplicationErrorConfig.getInvalidAgentSystemConfigurationErrorCode(), HttpStatus.BAD_REQUEST);
        }

        if (!this.systemConfigurationRepository.existsByKeyAndGroup(key, systemConfigurationGroup) && systemConfigurationGroup == SystemConfigurationGroup.OFFICE_BRANCH) {
            throw new ZeusRuntimeException(this.soteriaApplicationErrorConfig.getInvalidBranchTypeErrorMessage(), this.soteriaApplicationErrorConfig.getInvalidAgentSystemConfigurationErrorCode(), HttpStatus.BAD_REQUEST);
        }

//        throw new ZeusRuntimeException(this.soteriaApplicationErrorConfig.getInvalidAgentSystemConfigurationErrorMessage(), this.soteriaApplicationErrorConfig.getInvalidAgentSystemConfigurationErrorCode(), HttpStatus.BAD_REQUEST);

    }

    public List<SystemConfiguration> findByGroup(SystemConfigurationGroup systemConfigurationGroup) {
        return systemConfigurationRepository.findByGroup(systemConfigurationGroup);
    }


}
