package com.atri.puscerdas.service;

import com.atri.puscerdas.entity.Auth;
import com.atri.puscerdas.model.auth.RegisterRequest;
import com.atri.puscerdas.repository.AuthRepository;
import com.atri.puscerdas.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterRequest request){
        validationService.validate(request);

        if (authRepository.existsById(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registerd");
        }

        Auth auth = new Auth();
        auth.setUsername(request.getUsername());
        auth.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        auth.setRole(0); //0: SuperAdmin, 1:Admin, 2-99:User
        auth.setStatus(1); //0:Not Approved, 1:Approved, 2:Freeze

        authRepository.save(auth);
    }
}
