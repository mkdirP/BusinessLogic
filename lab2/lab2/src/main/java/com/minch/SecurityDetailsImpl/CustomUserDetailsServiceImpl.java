package com.minch.SecurityDetailsImpl;

import com.minch.entity.Role;
import com.minch.entity.Users;
import com.minch.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// 从数据库中加载用户信息。这里的用户数据存储在 Users 实体中。
@Service
@Slf4j
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = usersRepository.findByUsername(username);

        // log.info("-------------------------------Username: " + username.equals("R55555"));
        // log.info("-------------------------------Password: " + user.getPassword().equals("$2a$10$BTxiDfVzTY.RiXhxk4EIn.odGCyMtYQWyafOmia2j1AtGp.R99S92"));
        if(user == null ) {
            throw new UsernameNotFoundException("Couldn't find user");
        }
        for(Role role : user.getRoles()) {
            System.out.println(role.getName());
        }


        return new CustomUserDetailsImpl(user);
    }

}
