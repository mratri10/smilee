package com.atri.puscerdas.controller;

import com.atri.puscerdas.model.WebResponse;
import com.atri.puscerdas.model.auth.RegisterRequest;
import com.atri.puscerdas.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;
    @PostMapping(
            path = "/api/auth/superregist",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String>register(@RequestBody RegisterRequest request){
        authService.register(request);
        return WebResponse.<String>builder().data("OKE").build();
    }
}
