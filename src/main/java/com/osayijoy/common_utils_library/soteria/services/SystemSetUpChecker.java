package com.osayijoy.common_utils_library.soteria.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.registhentication.registration.enums.Status;
import com.osayijoy.common_utils_library.soteria.config.SoteriaConfig;
import com.osayijoy.common_utils_library.soteria.dto.RegisterUserRequestDTO;
import com.osayijoy.common_utils_library.soteria.dto.RoleDTO;
import com.osayijoy.common_utils_library.soteria.enums.SystemConfigurationGroup;
import com.osayijoy.common_utils_library.soteria.models.Authority;
import com.osayijoy.common_utils_library.soteria.models.Role;
import com.osayijoy.common_utils_library.soteria.models.SystemConfiguration;
import com.osayijoy.common_utils_library.soteria.repositories.AuthorityRepository;
import com.osayijoy.common_utils_library.soteria.repositories.RoleRepository;
import com.osayijoy.common_utils_library.soteria.repositories.SystemConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
@Slf4j
public class SystemSetUpChecker implements ApplicationListener<ContextRefreshedEvent> {

    private final UserDao userDao;
    private final RoleRepository roleRepository;

    private final AuthorityRepository authorityRepository;

    private final SystemConfigurationRepository systemConfigurationRepository;

    private final SoteriaConfig soteriaConfig;



    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        readRoles();
        createSystemAdmin();
    }

    private void createSystemAdmin() {
        try {
            userDao.selectUserByUsername(soteriaConfig.getSystemAdminRoleName());
        }catch (ZeusRuntimeException e){
            log.info(">>>>> About to create System Admin <<<<<<");
            RegisterUserRequestDTO registerUserRequestDTO = new RegisterUserRequestDTO();
            registerUserRequestDTO.setPassword("P@ssw0rd");
            registerUserRequestDTO.setUsername(soteriaConfig.getSystemAdminRoleName());
            registerUserRequestDTO.setFirstName(soteriaConfig.getSystemAdminRoleName());
            registerUserRequestDTO.setLastName(soteriaConfig.getSystemAdminRoleName());
            registerUserRequestDTO.setEmail(soteriaConfig.getSystemAdminEmail());
            registerUserRequestDTO.setRoles(RoleDTO.builder().roleName(soteriaConfig.getSystemAdminRoleName()).build());
            try {
                userDao.registerUser(registerUserRequestDTO, Status.ACTIVE);
                log.info(">>>>> System Admin Created Successfully <<<<<<");
                createDefaultSystemConfigs();
            } catch (ZeusRuntimeException ex) {
                log.error(">>>>> Unable to create System Admin <<<<<<");
                throw new ZeusRuntimeException(soteriaConfig.getSystemAdminRoleName().concat("already exist"));
            }
        }
    }

    private void createDefaultSystemConfigs(){
        log.info(">>>>> About to create System Configuration <<<<<<");
        SystemConfiguration systemConfiguration = new SystemConfiguration();
        systemConfiguration.setKey("login.max.attempts");
        systemConfiguration.setValue("3");
        systemConfiguration.setGroup(SystemConfigurationGroup.LOGIN);
        systemConfigurationRepository.save(systemConfiguration);
        log.info(">>>>> System Configuration Created Successfully <<<<<<");
    }


    private void readRoles(){
        log.info(">>>>> About to create System Roles <<<<<<");
        ObjectMapper mapper = new ObjectMapper();

        soteriaConfig.getSystemGeneratedRolesPath().forEach(roles -> saveRole(Paths.get(roles).toFile(),mapper));
        log.info(">>>>> System Roles Created Successfully <<<<<<");

    }

    public void saveRole(File file, ObjectMapper mapper) {

        try {
            Role role = mapper.readValue(file, Role.class);
            Role existingRole = roleRepository.findByName(role.getName());
            List<Authority> authorities;
            authorities = getAuthorities(Objects.requireNonNullElse(existingRole, role), role);
            role = Objects.requireNonNullElse(existingRole, role);
            role.setPermissions(authorities);
            if (existingRole != null) {
                role.setName(role.getName());
                role.setRoleScope(role.getRoleScope());
                role.setDescription(role.getDescription());
            }
            log.trace(">>>>> About to save default system role : {} <<<<<<",role.getName());
            roleRepository.save(role);
        } catch (IOException e) {
            throw new ZeusRuntimeException(e.getMessage());
        }
    }

        private List<Authority> getAuthorities(Role existingRole,Role updateRole) {
            List<Authority> finalAuthorities = (List<Authority>) updateRole.getPermissions();
            Set<Authority> authorities = new HashSet<>(existingRole.getPermissions());

            finalAuthorities.forEach(x->{
                List<Authority> authority = authorityRepository.findAllByPermission(x.getPermission());
                if (authority.isEmpty()){
                    authorities.add(x);
                }
            });

            return new ArrayList<>(authorities);
        }


}

