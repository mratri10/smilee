package com.atri.puscerdas.repository;

import com.atri.puscerdas.entity.Auth;
import com.atri.puscerdas.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, String> {
    Optional<ResetPassword> findByUsername(String username);
}
