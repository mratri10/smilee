package com.atri.puscerdas.resolver;

import com.atri.puscerdas.entity.Auth;
import com.atri.puscerdas.repository.AuthRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {


    @Autowired
    private AuthRepository authRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Auth.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = servletRequest.getHeader("X-API-TOKEN");

        if(token == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        Auth auth = authRepository.findFirstByToken(token)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        log.info(auth.toString());
        if(auth.getTokenExp() < System.currentTimeMillis()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return auth;
    }
}
