package com.atri.puscerdas.controller.auth;

import com.atri.puscerdas.entity.Auth;
import com.atri.puscerdas.model.TokenResponse;
import com.atri.puscerdas.model.WebResponse;
import com.atri.puscerdas.model.auth.LoginRequest;
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
public class LoginControllerTest {
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
    void testLoginByUsernameSuccess() throws Exception{
        Auth auth = new Auth();
        auth.setUsername("atri10");
        auth.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        auth.setEmail("atri@gmail.com");
        auth.setPhone("08113333");
        auth.setRole(1);
        auth.setStatus(1);

        authRepository.save(auth);

        LoginRequest request = new LoginRequest();
        request.setUsername("atri10");
        request.setPassword("123456");

        mockMvc.perform(
                post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TokenResponse>response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertNotNull(response.getData().getToken());

            Auth authDb = authRepository.findById("atri10").orElse(null);
            assertNotNull(authDb);
            assertEquals(authDb.getToken(), response.getData().getToken());
        });
    }

}
