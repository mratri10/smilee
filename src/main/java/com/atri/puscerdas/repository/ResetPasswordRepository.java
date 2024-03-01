package com.atri.puscerdas.repository;

import com.atri.puscerdas.entity.Auth;
import com.atri.puscerdas.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, String> {
}
