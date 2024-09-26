package com.minch.service;


import com.minch.dto.LoginDTO;
import com.minch.dto.RegistDTO;
import com.minch.entity.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UsersService {

    ResponseEntity<?> addUsers(RegistDTO registDTO);
    Users getCurrentUser();

    List<Users> findAllAdminsByRoles(String role);
}
