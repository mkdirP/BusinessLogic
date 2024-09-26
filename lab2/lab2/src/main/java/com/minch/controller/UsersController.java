package com.minch.controller;

import com.minch.dto.LoginDTO;
import com.minch.dto.RegistDTO;
import com.minch.entity.Users;
import com.minch.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    private UsersService usersService;

    // 注册用户
    @PostMapping("/register")
    public ResponseEntity<String> addUsers(@Valid @RequestBody RegistDTO registDTO) {
        try {
            usersService.addUsers(registDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 返回错误提示信息
        }
    }

}
