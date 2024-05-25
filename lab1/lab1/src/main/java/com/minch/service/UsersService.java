package com.minch.service;


import com.minch.model.Users;

public interface UsersService {
    Users addUsers(Users u);

    Users updateUsers(Integer id, Users u);

    Users loginUsers(Users u);
    Users logoutUsers(Users u);

    boolean existsUsers(Users u);

}
