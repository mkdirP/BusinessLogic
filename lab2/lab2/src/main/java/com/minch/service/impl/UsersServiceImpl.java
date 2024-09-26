package com.minch.service.impl;

import com.minch.dto.LoginDTO;
import com.minch.dto.RegistDTO;
import com.minch.entity.EnumRole;
import com.minch.entity.Role;
import com.minch.entity.Users;
import com.minch.exception.ResourceNotFoundException;
import com.minch.repository.RoleRepository;
import com.minch.repository.UsersRepository;
import com.minch.service.UsersService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Slf4j
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public ResponseEntity<?> addUsers(@Valid @RequestBody RegistDTO registDTO) {

        Users user = new Users();

        // 检查用户名是否已存在
        if (usersRepository.existsByUsername(registDTO.getUsername())) {
            throw new RuntimeException("The username is exists"); // 提示用户名已存在
        }else {
            user.setUsername(registDTO.getUsername());
        }

        user.setPassword(passwordEncoder.encode(registDTO.getPassword())); // 加密密码

        // 检查邮箱是否已存在
        if (usersRepository.existsByEmail(registDTO.getEmail())) {
            throw new RuntimeException("This email is already registered");
        } else {
            user.setEmail(registDTO.getEmail());
        }

        Set<Role> roleSet = new HashSet<>();
        Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                // .orElseGet(() -> {
                //     Role newRole = new Role();
                //     newRole.setName(EnumRole.ROLE_USER);
                //     return roleRepository.save(newRole);
                // });
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roleSet.add(userRole);

        // 将角色分配给用户
        user.setRoles(roleSet);
        usersRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @Override
    public Users getCurrentUser() {
        // 获取当前用户的认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        return usersRepository.findByUsername(username);
    }

    @Override
    public List<Users> findAllAdminsByRoles(String role) {
        return usersRepository.findAllByRolesName(EnumRole.valueOf(role));
    }


}
