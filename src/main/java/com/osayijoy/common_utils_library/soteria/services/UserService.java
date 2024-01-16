package com.osayijoy.common_utils_library.soteria.services;

import java.util.HashSet;
import java.util.Set;

import com.osayijoy.common_utils_library.soteria.dto.AuthorityDTO;
import com.osayijoy.common_utils_library.soteria.dto.RoleDTO;
import com.osayijoy.common_utils_library.soteria.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userDao.selectUserByUsername(username);
        userDTO.setAuthorities(getGrantedAuthorities(userDTO.getRoleDTO()));
        return userDTO;
    }

    private Set<SimpleGrantedAuthority> getGrantedAuthorities(RoleDTO roleDTO) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (AuthorityDTO privilege : roleDTO.getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(privilege.getPermission()));
        }
        return authorities;
    }
}