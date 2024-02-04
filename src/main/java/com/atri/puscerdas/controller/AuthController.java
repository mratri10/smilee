package com.atri.puscerdas.controller;

import com.atri.puscerdas.entity.Auth;
import com.atri.puscerdas.model.TokenResponse;
import com.atri.puscerdas.model.WebResponse;
import com.atri.puscerdas.model.auth.*;
import com.atri.puscerdas.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping(
            path = "/api/auth/superregist",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String>registerSuper(@RequestBody RegisterRequest request){
        authService.registerSuper(request);
        return WebResponse.<String>builder().data("OKE").build();
    }

    @PostMapping(
            path = "/api/auth/employee-regist",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String>registerEmployee(@RequestBody RegisterEmployeeRequest request){
        authService.registerEmployee(request);
        return  WebResponse.<String>builder().data("OKE").build();
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
}
