package com.e_commerce.service.impl;

import com.e_commerce.api.dto.AuthCreateUserRequest;
import com.e_commerce.api.dto.AuthResponse;
import com.e_commerce.model.entity.Role;
import com.e_commerce.model.entity.RoleEnum;
import com.e_commerce.model.entity.UserEntity;
import com.e_commerce.model.repository.RoleDAO;
import com.e_commerce.model.repository.UserEntityDAO;
import com.e_commerce.service.UserEntityService;
import com.e_commerce.util.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserEntityServiceImpl implements UserEntityService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserEntityDAO userEntityDAO;
    private final RoleDAO roleDAO;
    private final JwtUtils jwtUtils;

    public UserEntityServiceImpl(PasswordEncoder passwordEncoder, UserEntityDAO userEntityDAO, RoleDAO roleDAO, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userEntityDAO = userEntityDAO;
        this.roleDAO = roleDAO;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public AuthResponse createUser(AuthCreateUserRequest createUserRequest) {
        String username = createUserRequest.username();
        String password = createUserRequest.password();

        String roleString = "USER";
        RoleEnum roleEnum = RoleEnum.valueOf(roleString);
        Set<Role> rol = new HashSet<>(roleDAO.findRoleByRoleEnumIs(roleEnum));

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(createUserRequest.email())
                .firstName(createUserRequest.firstName())
                .lastName(createUserRequest.lastName())
                .roles(rol)
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .build();

        UserEntity userSaved = userEntityDAO.save(userEntity);

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userSaved.getRoles().forEach(role ->
            authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())))
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(userEntity.getUsername(), userEntity.getPassword(), authorities);

        String accesToken = jwtUtils.createToken(auth);

        return new AuthResponse(username, "User created succesfully", accesToken, true);
    }

    @Override
    public Optional<UserEntity> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<UserEntity> findAll() {
        return List.of();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userEntityDAO.findUserEntityByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User " + username + " not found")
        );

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userEntity.getRoles()
                .forEach(role ->
                        authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())))
                );

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoExpired(),
                authorities);
    }
}
