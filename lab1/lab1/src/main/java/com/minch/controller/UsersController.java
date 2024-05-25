package com.minch.controller;

import com.minch.model.Users;
import com.minch.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/register")
    public ResponseEntity<String> addUsers(@RequestBody Users u) {
        usersService.addUsers(u);
        return ResponseEntity.status(HttpStatus.CREATED).body("User successful registration");
    }

    @PutMapping("/{id}")
    // 从请求路径中提取参数值
    public ResponseEntity<String> updateUsers(@PathVariable Integer id, @RequestBody Users u){
        usersService.updateUsers(id, u);
        return ResponseEntity.ok("User information updated successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsers(@RequestBody Users u){
        Users user = usersService.loginUsers(u);

        if (user != null){
            // 用户存在并且用户名密码匹配
            return ResponseEntity.ok("Successful login");
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password wrong");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUsers(@RequestBody Users u){
        usersService.logoutUsers(u);
        return ResponseEntity.ok().body("Successful logout");

    }

}
