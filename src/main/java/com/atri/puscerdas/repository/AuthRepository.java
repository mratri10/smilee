package com.atri.puscerdas.repository;

import com.atri.puscerdas.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, String> {
    Optional<Auth> findFirstByToken(String token);
    Optional<Auth> findByEmail(String email);
    Optional<Auth> findByPhone(String phone);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
