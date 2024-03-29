package com.atri.puscerdas.service;

import com.atri.puscerdas.entity.Auth;
import com.atri.puscerdas.entity.ResetPassword;
import com.atri.puscerdas.model.TokenResponse;
import com.atri.puscerdas.model.auth.*;
import com.atri.puscerdas.repository.AuthRepository;
import com.atri.puscerdas.repository.ResetPasswordRepository;
import com.atri.puscerdas.security.BCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    ResetPasswordRepository resetPasswordRepository;

    @Autowired
    ValidationService validationService;


    @Transactional
    public AuthResponse akuService(Auth auth){
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUsername(auth.getUsername());
        authResponse.setEmail(auth.getEmail());
        authResponse.setPhone(auth.getPhone());
        authResponse.setRole(auth.getRole());
        authResponse.setStatus(auth.getStatus());
        return authResponse;
    }
    @Transactional
    public void registerSuper(RegisterRequest request){
        validationService.validate(request);

        if (authRepository.existsById(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already register");
        }

        Auth auth = new Auth();
        auth.setUsername(request.getUsername());
        auth.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        auth.setRole(0); //0: SuperAdmin, 1:Admin, 2-99:User
        auth.setStatus(1); //0:Not Approved, 1:Approved, 2:Freeze

        authRepository.save(auth);
    }

    @Transactional
    public void registerEmployee(Auth auth,RegisterEmployeeRequest request){
        validationService.validate(request);

        if (authRepository.existsById(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already register");
        }

        if (authRepository.existsByEmail(request.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already register");
        }

        if (authRepository.existsByPhone(request.getPhone())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone already register");
        }

        Auth employeeAuth = new Auth();
        employeeAuth.setUsername(request.getUsername());
        employeeAuth.setPassword(BCrypt.hashpw("pus12345", BCrypt.gensalt()));
        employeeAuth.setEmail(request.getEmail());
        employeeAuth.setPhone(request.getPhone());
        employeeAuth.setRole(1); //0: SuperAdmin, 1:Admin, 2-99:User
        employeeAuth.setStatus(1); //0:Not Approved, 1:Approved, 2:Freeze

        authRepository.save(employeeAuth);
    }

    @Transactional
    public AuthResponse update(Auth auth, UpdateAuthRequest request){
        validationService.validate(request);

        if(Objects.nonNull(request.getEmail())){
            auth.setEmail(request.getEmail());
        }
        if(Objects.nonNull(request.getPhone())){
            auth.setPhone(request.getPhone());
        }
        authRepository.save(auth);
        return AuthResponse.builder()
                .username(auth.getUsername())
                .email(auth.getEmail())
                .phone(auth.getPhone())
                .role(auth.getRole())
                .status(auth.getStatus())
                .build();
    }

    @Transactional
    public TokenResponse login(LoginRequest request){
        validationService.validate(request);
        Auth auth = new Auth();
        if(Objects.nonNull(request.getUsername())){
            auth = authRepository.findById(request.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username Is Not Found"));
        }else if(Objects.nonNull(request.getEmail())){
            Optional<Auth> authOptional = authRepository.findByEmail(request.getEmail());
            if(authOptional.isPresent()){
                auth = authOptional.get();
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is Not Found");
            }
        }else if(Objects.nonNull(request.getPhone())){
            Optional<Auth> authOptional = authRepository.findByPhone(request.getPhone());
            if(authOptional.isPresent()){
                auth = authOptional.get();
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone is Not Found");
            }
        }
        if(BCrypt.checkpw(request.getPassword(), auth.getPassword())){
            auth.setToken(UUID.randomUUID().toString());
            auth.setTokenExp(next10Days());
            authRepository.save(auth);

            return TokenResponse.builder()
                    .token(auth.getToken())
                    .tokenExp(auth.getTokenExp())
                    .build();
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password is Wrong");
        }
    }

    private Long next10Days(){
        return  System.currentTimeMillis()+(1000 * 60 * 60 * 24 * 10);
    }
    @Transactional
    public String resetPassword(ResetRequest request){
        validationService.validate(request);

        String id = UUID.randomUUID().toString();

        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setId(id);
        resetPassword.setUsername(request.getUsername());
        resetPassword.setIdExp(next2hours());

        resetPasswordRepository.save(resetPassword);

        return id;
    }

    @Transactional
    public ResetPassword detailResetPassword(Auth auth, String username){
        return resetPasswordRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public List<ResetPassword> listResetPassword(Auth auth){
        List<ResetPassword> resetPasswords = resetPasswordRepository.findAll();
        return  resetPasswords;
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request){
        validationService.validate(request);
        ResetPassword resetPassword = resetPasswordRepository.findById(request.getIdReset()).orElse(null);
        if(resetPassword !=null && System.currentTimeMillis() < resetPassword.getIdExp()){
            Auth auth = authRepository.findById(resetPassword.getUsername()).orElse(null);
            if(auth != null){
                auth.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
                authRepository.save(auth);
                resetPasswordRepository.deleteById(request.getIdReset());
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not Found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID ini sudah tidak berlaku");
        }
    }

    @Transactional
    public List<AuthResponse> getListEmployee(Auth auth){
        List<Auth> authList = authRepository.findByRole(1);

        return authList.stream()
                .map(this::mapAuthToResponse)
                .toList();
    }

    private Long next2hours(){
        return  System.currentTimeMillis()+(1000 * 60 * 60 * 2);
    }

    private AuthResponse mapAuthToResponse(Auth auth){
        AuthResponse authResponse = new AuthResponse();
        authResponse.setEmail(auth.getEmail());
        authResponse.setPhone(auth.getPhone());
        authResponse.setUsername(auth.getUsername());
        authResponse.setRole(auth.getRole());

        return authResponse;
    }

}


