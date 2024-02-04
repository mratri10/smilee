package com.atri.puscerdas.controller.auth;

import com.atri.puscerdas.entity.Auth;
import com.atri.puscerdas.model.WebResponse;
import com.atri.puscerdas.model.auth.AuthResponse;
import com.atri.puscerdas.model.auth.RegisterEmployeeRequest;
import com.atri.puscerdas.model.auth.RegisterRequest;
import com.atri.puscerdas.model.auth.UpdateAuthRequest;
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
            assertEquals("password: Minimal Character is 5", response.getErrors());
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
            assertEquals("Username already register", response.getErrors());
        });
    }

    @Test
    void testRegisterEmployeeSuccess() throws Exception{
        RegisterEmployeeRequest request = new RegisterEmployeeRequest();
        request.setUsername("atri10");
        request.setEmail("atri@gmail.com");
        request.setPhone("08126373839");
        mockMvc.perform(
                post("/api/auth/employee-regist")
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
    void testRegisterEmployeeEmpty() throws Exception {
        RegisterEmployeeRequest request = new RegisterEmployeeRequest();
        request.setUsername("");
        request.setEmail("atri@gmail.com");
        request.setPhone("08126373839");

        mockMvc.perform(
                post("/api/auth/employee-regist")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterEmployeeBad() throws Exception {
        RegisterEmployeeRequest request = new RegisterEmployeeRequest();
        request.setUsername("atri10");
        request.setEmail("atrigmailcom");
        request.setPhone("08126373839");

        mockMvc.perform(
                post("/api/auth/employee-regist")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNotNull(response.getErrors());
            assertEquals("email: must be a well-formed email address", response.getErrors());
        });
    }

    @Test
    void testRegisterEmployeeExist() throws Exception {
        Auth auth = new Auth();
        auth.setUsername("atri10");
        auth.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        auth.setRole(1);
        auth.setStatus(1);
        authRepository.save(auth);

        RegisterEmployeeRequest request = new RegisterEmployeeRequest();
        request.setUsername("atri10");
        request.setEmail("atri@gmail.com");
        request.setPhone("08126373839");

        mockMvc.perform(
                post("/api/auth/employee-regist")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNotNull(response.getErrors());
            assertEquals("Username already register", response.getErrors());
        });
    }

    @Test
    void testRegisterUpdateSuccess() throws Exception {
        Auth auth = new Auth();
        auth.setUsername("atri10");
        auth.setEmail("atri@gmail.com");
        auth.setPhone("0811123");
        auth.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        auth.setRole(1);
        auth.setStatus(1);
        auth.setToken("abcdefgh");
        auth.setTokenExp(System.currentTimeMillis()+1000000000);
        authRepository.save(auth);

        UpdateAuthRequest request = new UpdateAuthRequest();
        request.setEmail("atri10@gmail.com");
        request.setPhone("0811111");

        mockMvc.perform(
                patch("/api/auth/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN","abcdefgh")
        ).andExpect(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNull(response.getErrors());
            assertEquals("atri10@gmail.com", response.getData().getEmail());
            assertEquals("0811111", response.getData().getPhone());
        });
    }
    @Test
    void testRegisterUpdateExpired() throws Exception {
        Auth auth = new Auth();
        auth.setUsername("atri10");
        auth.setEmail("atri@gmail.com");
        auth.setPhone("0811123");
        auth.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        auth.setRole(1);
        auth.setStatus(1);
        auth.setToken("abcdefgh");
        auth.setTokenExp(System.currentTimeMillis()-1000000000);
        authRepository.save(auth);

        UpdateAuthRequest request = new UpdateAuthRequest();
        request.setEmail("atri10@gmail.com");
        request.setPhone("0811111");

        mockMvc.perform(
                patch("/api/auth/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN","abcdefgh")
        ).andExpect(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterUpdateWrongToken() throws Exception {
        Auth auth = new Auth();
        auth.setUsername("atri10");
        auth.setEmail("atri@gmail.com");
        auth.setPhone("0811123");
        auth.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        auth.setRole(1);
        auth.setStatus(1);
        auth.setToken("abcdefgh");
        auth.setTokenExp(System.currentTimeMillis()-1000000000);
        authRepository.save(auth);

        UpdateAuthRequest request = new UpdateAuthRequest();
        request.setEmail("atri10@gmail.com");
        request.setPhone("0811111");

        mockMvc.perform(
                patch("/api/auth/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN","abcdefghdsdfs")
        ).andExpect(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterUpdateEmailOnlySuccess() throws Exception {
        Auth auth = new Auth();
        auth.setUsername("atri10");
        auth.setEmail("atri@gmail.com");
        auth.setPhone("0811123");
        auth.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        auth.setRole(1);
        auth.setStatus(1);
        auth.setToken("abcdefgh");
        auth.setTokenExp(System.currentTimeMillis()+1000000000);
        authRepository.save(auth);

        UpdateAuthRequest request = new UpdateAuthRequest();
        request.setEmail("atri10@gmail.com");

        mockMvc.perform(
                patch("/api/auth/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN","abcdefgh")
        ).andExpect(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNull(response.getErrors());
            assertEquals("atri10@gmail.com", response.getData().getEmail());
            assertEquals("0811123", response.getData().getPhone());
        });
    }
}
