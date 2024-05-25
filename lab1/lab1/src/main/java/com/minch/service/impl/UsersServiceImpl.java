package com.minch.service.impl;

import com.minch.repository.UsersRepository;
import com.minch.model.Users;
import com.minch.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Users addUsers(@RequestBody Users u) {
        // 检查用户名是否为空
        if (u.getUsername() == null || u.getUsername().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        // 检查邮箱是否为空
        if (u.getEmail() == null || u.getEmail().isEmpty()) {
            throw new RuntimeException("邮箱不能为空");
        }
        if (usersRepository.existsByUsername(u.getUsername())){
            throw new RuntimeException("用户名已被使用");
        }
        if (usersRepository.existsByEmail(u.getEmail())){
            throw new RuntimeException("邮箱已被注册");
        }
        return usersRepository.save(u);

    }

    // @Override
    // public void deleteUsers(Integer id) {
    //     usersRepository.deleteById(id);
    // }

    @Override
    public Users updateUsers(Integer id, Users u) {
        // 检查用户是否存在
        if (usersRepository.findById(id).orElse(null) == null){
            throw new RuntimeException("用户不存在");
        }
        u.setId(u.getId());
        return usersRepository.save(u);
    }

    public Users loginUsers(Users u){
        String username = u.getUsername();
        String password = u.getPassword();
    //     根据用户名查询用户信息
        Users user = usersRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)){
            // 用户存在并且用户名密码匹配
            return user;
        }else {
            return null;
        }

    }

    public Users logoutUsers(Users u){
        Users user = loginUsers(u);
        if (user == null){
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    @Override
    public boolean existsUsers(Users u) {
        Users user = loginUsers(u);
        if (user != null){
            return true;
        }
        return false;
    }

}
