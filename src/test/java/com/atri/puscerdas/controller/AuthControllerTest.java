package com.atri.puscerdas.controller;

import com.atri.puscerdas.entity.Auth;
import com.atri.puscerdas.model.WebResponse;
import com.atri.puscerdas.model.auth.RegisterRequest;
import com.atri.puscerdas.repository.AuthRepository;
import com.atri.puscerdas.security.BCrypt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthRepository authRepository;

    @BeforeEach
    void setUp(){
        authRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception{
        RegisterRequest request = new RegisterRequest();
        request.setUsername("atri10");
        request.setPassword("123456");
        mockMvc.perform(
                post("/api/auth/superregist")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("OKE", response.getData());
        });
    }

    @Test
    void testRegisterEmpty() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("atri10");
        request.setPassword("");

        mockMvc.perform(
                post("/api/auth/superregist")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNotNull(response.getErrors());
            assertEquals("password: Minimal Charecter is 5, password: must not be blank", response.getErrors());
        });
    }

    @Test
    void testRegisterBad() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("atri10");
        request.setPassword("12");

        mockMvc.perform(
                post("/api/auth/superregist")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNotNull(response.getErrors());
            assertEquals("password: Minimal Charecter is 5", response.getErrors());
        });
    }

    @Test
    void testRegisterExist() throws Exception {
        Auth auth = new Auth();
        auth.setUsername("atri10");
        auth.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        auth.setRole(0);
        auth.setStatus(1);
        authRepository.save(auth);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("atri10");
        request.setPassword("123456");

        mockMvc.perform(
                post("/api/auth/superregist")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNotNull(response.getErrors());
            assertEquals("Username already registerd", response.getErrors());
        });
    }
}
