package com.minch.repository;

import com.minch.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    // 封装一个可能为 null 的对象。避免在代码中出现空指针异常
    Optional<Users> findById(Integer id);
    Users findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Users save(Users u);

}
