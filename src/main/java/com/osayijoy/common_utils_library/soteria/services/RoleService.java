package com.osayijoy.common_utils_library.soteria.services;

import java.util.List;

import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.soteria.api_models.response.RoleApiModel;
import com.osayijoy.common_utils_library.soteria.config.SoteriaApplicationErrorConfig;
import com.osayijoy.common_utils_library.soteria.dto.AuthorityDTO;
import com.osayijoy.common_utils_library.soteria.dto.RoleDTO;
import com.osayijoy.common_utils_library.soteria.enums.RoleScope;
import com.osayijoy.common_utils_library.soteria.models.Authority;
import com.osayijoy.common_utils_library.soteria.models.Role;
import com.osayijoy.common_utils_library.soteria.repositories.AuthorityRepository;
import com.osayijoy.common_utils_library.soteria.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final SoteriaApplicationErrorConfig errorConfig;

    public List<String> fetchRoles(){
       return roleRepository.findAllByRoleScopeAndIsActive(List.of(RoleScope.FOR_BACKOFFICE.toString()), Boolean.TRUE).stream().map(this::buildRoleName).toList();
    }

    public void createRoleWithPermissions(RoleDTO roleDTO) {
        saveRole(roleDTO);
    }

    public void updateRoleWithPermissions(RoleApiModel roleApiModel) {
        updateRole(roleApiModel);
    }
    private String buildRoleName(Role role){
        return role.getName();
    }


    public void deleteRole(String roleName){
        Role roleToDelete = roleRepository.findByName(roleName);
        if (roleToDelete != null){
            roleToDelete.setActive(false);
            roleRepository.save(roleToDelete);
            return;
        }
        throw new ZeusRuntimeException(errorConfig.getRoleNotFoundMessage(), errorConfig.getRoleNotFoundErrorCode(),HttpStatus.BAD_REQUEST);

    }

    public RoleDTO getRoleWithPermissions(String roleName){
       return getRole(roleName);
    }




    public void updateRole(RoleApiModel roleApiModel){
        Role roleToUpdate = roleRepository.findByName(roleApiModel.getRoleName());
        if (roleToUpdate != null) {
            roleToUpdate.setDescription(roleApiModel.getRoleDescription());
            if (roleApiModel.getPermissions() != null && !roleApiModel.getPermissions().isEmpty()) {
                roleToUpdate.setPermissions(createSharedListViaStream((List<Authority>) roleToUpdate.getPermissions(), roleApiModel.getPermissions()));
            }
            roleRepository.save(roleToUpdate);
        }else
            throw new ZeusRuntimeException(errorConfig.getRoleNotFoundMessage(), errorConfig.getRoleNotFoundErrorCode(),HttpStatus.BAD_REQUEST);

    }


    private void saveRole(RoleDTO roleDTO){
        Role role = new Role();
        if(roleDTO.getPermissions()!=null && !roleDTO.getPermissions().isEmpty()){
            role.setActive(Boolean.TRUE);
            role.setDescription(roleDTO.getDescription());
            role.setName(roleDTO.getRoleName());
            role.setRoleScope(RoleScope.FOR_BACKOFFICE);
            role.setPermissions(createSharedListFromRepositoryViaStream(roleDTO.getPermissions()));
            roleRepository.save(role);
            return;

        }
        throw new ZeusRuntimeException(errorConfig.getRoleNotFoundMessage(), errorConfig.getRoleNotFoundErrorCode(),HttpStatus.BAD_REQUEST);

    }

    private RoleDTO getRole(String userRole){
        Role role = roleRepository.findByName(userRole);
        if (role != null){
          return  RoleDTO.builder()
                    .roleName(role.getName())
                    .description(role.getDescription())
                    .permissions(role.getPermissions().stream().map(authority -> AuthorityDTO.builder()
                              .permission(authority.getPermission())
                              .permissionType(authority.getPermissionType())
                              .permissionDescription(authority.getDescription())
                              .build()).toList())
                    .build();

        } else
            throw new ZeusRuntimeException(errorConfig.getRoleNotFoundMessage(), errorConfig.getRoleNotFoundErrorCode(),HttpStatus.BAD_REQUEST);
    }


    public Role getRole(RoleDTO userRole){
        Role role = roleRepository.findByName(userRole.getRoleName());
        if (role != null){
            return  role;
        } else
            throw new ZeusRuntimeException(errorConfig.getRoleNotFoundMessage(), errorConfig.getRoleNotFoundErrorCode(),HttpStatus.BAD_REQUEST);
    }

    public static List<Authority> createSharedListViaStream(List<Authority> listOne, List<String> listTwo) {
        return listOne.stream()
                .filter(two -> listTwo.stream()
                        .anyMatch(one -> one.equalsIgnoreCase(two.getPermission())))
                .toList();
    }


    public List<Authority> createSharedListFromRepositoryViaStream(List<AuthorityDTO> listTwo) {
        return authorityRepository.findAll().stream()
                .filter(two -> listTwo.stream()
                        .anyMatch(one -> one.getPermission().equalsIgnoreCase(two.getPermission())))
                .toList();
    }

}
