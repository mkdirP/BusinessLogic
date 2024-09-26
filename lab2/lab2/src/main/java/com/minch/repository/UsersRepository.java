package com.minch.repository;

import com.minch.entity.EnumRole;
import com.minch.entity.Role;
import com.minch.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    // 封装一个可能为 null 的对象。避免在代码中出现空指针异常
    Optional<Users> findById(Integer id);
    Users findByUsername(String username);
    // Optional<Users> findUsersByEmail(String email);

    List<Users> findAllByRolesName(EnumRole role);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Users save(Users u);

}
