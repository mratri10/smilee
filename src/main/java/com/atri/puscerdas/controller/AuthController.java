package com.atri.puscerdas.controller;

import com.atri.puscerdas.entity.Auth;
import com.atri.puscerdas.entity.ResetPassword;
import com.atri.puscerdas.model.TokenResponse;
import com.atri.puscerdas.model.WebResponse;
import com.atri.puscerdas.model.auth.*;
import com.atri.puscerdas.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;


    @GetMapping(
            path = "/api/aku"
    )
    public WebResponse<AuthResponse>apiAku(Auth auth){
        AuthResponse authResponse = authService.akuService(auth);
        return WebResponse.<AuthResponse>builder().data(authResponse).build();
    }
    @PostMapping(
            path = "/api/auth/superregist",
            consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String>registerSuper(@RequestBody RegisterRequest request){
        authService.registerSuper(request);
        return WebResponse.<String>builder().data("OKE Atri").build();
    }

    @PostMapping(
            path = "/api/auth/employee-regist",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String>registerEmployee(Auth auth,@RequestBody RegisterEmployeeRequest request){

        authService.registerEmployee(auth,request);
        return  WebResponse.<String>builder().data("OKE").build();
    }

    @GetMapping(
            path = "/api/auth/employee-regist"
    )
    public WebResponse<List<AuthResponse>>getEmployee(Auth auth){
        List<AuthResponse> responseList= authService.getListEmployee(auth);
        return  WebResponse.<List<AuthResponse>>builder().data(responseList).build();
    }

    @PatchMapping(
            path = "/api/auth/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AuthResponse>updateAuth(Auth auth,
                                         @RequestBody UpdateAuthRequest request){

        AuthResponse authResponse = authService.update(auth,request);
        return  WebResponse.<AuthResponse>builder().data(authResponse).build();
    }

    @PostMapping(
            path = "/api/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse>login(@RequestBody LoginRequest request){
        TokenResponse tokenResponse = authService.login(request);
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }
    @PostMapping(
            path = "/api/reset-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String>resetPassword(@RequestBody ResetRequest request){
        authService.resetPassword(request);
        return WebResponse.<String>builder().data("OKE").build();
    }

    @GetMapping(
            path = "/api/reset-password-list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ResetPassword>>listResetPassword(Auth auth){
        List<ResetPassword> resetPasswords= authService.listResetPassword(auth);
        return WebResponse.<List<ResetPassword>>builder().data(resetPasswords).build();
    }
    @PostMapping(
            path = "/api/change-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String>changePassword(@RequestBody ChangePasswordRequest request){
        authService.changePassword(request);
        return  WebResponse.<String>builder().data("OKE").build();
    }

}
