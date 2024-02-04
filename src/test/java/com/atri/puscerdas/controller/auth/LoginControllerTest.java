package com.atri.puscerdas.controller.auth;

import com.atri.puscerdas.entity.Auth;
import com.atri.puscerdas.entity.ResetPassword;
import com.atri.puscerdas.model.TokenResponse;
import com.atri.puscerdas.model.WebResponse;
import com.atri.puscerdas.model.auth.ChangePasswordRequest;
import com.atri.puscerdas.model.auth.LoginRequest;
import com.atri.puscerdas.model.auth.ResetRequest;
import com.atri.puscerdas.repository.AuthRepository;
import com.atri.puscerdas.repository.ResetPasswordRepository;
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
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AuthRepository authRepository;

    @Autowired
    ResetPasswordRepository resetPasswordRepository;
    @BeforeEach
    void setUp(){
        authRepository.deleteAll();
        resetPasswordRepository.deleteAll();
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
            assertTrue(response.getData().getTokenExp() > 10000);

            Auth authDb = authRepository.findById("atri10").orElse(null);
            assertNotNull(authDb);
            assertEquals(authDb.getToken(), response.getData().getToken());
            assertEquals(authDb.getTokenExp(), response.getData().getTokenExp());
        });
    }

    @Test
    void testLoginByEmailSuccess() throws Exception{
        Auth auth = new Auth();
        auth.setUsername("atri10");
        auth.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        auth.setEmail("atri@gmail.com");
        auth.setPhone("08113333");
        auth.setRole(1);
        auth.setStatus(1);

        authRepository.save(auth);

        LoginRequest request = new LoginRequest();
        request.setEmail("atri@gmail.com");
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
            assertTrue(response.getData().getTokenExp() > 10000);

            Auth authDb = authRepository.findByEmail("atri@gmail.com").orElse(null);
            assertNotNull(authDb);
            assertEquals(authDb.getToken(), response.getData().getToken());
            assertEquals(authDb.getTokenExp(), response.getData().getTokenExp());
        });
    }
    @Test
    void testLoginByPhoneSuccess() throws Exception{
        Auth auth = new Auth();
        auth.setUsername("atri10");
        auth.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        auth.setEmail("atri@gmail.com");
        auth.setPhone("08113333");
        auth.setRole(1);
        auth.setStatus(1);

        authRepository.save(auth);

        LoginRequest request = new LoginRequest();
        request.setPhone("08113333");
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
            assertTrue(response.getData().getTokenExp() > 10000);

            Auth authDb = authRepository.findByPhone("08113333").orElse(null);
            assertNotNull(authDb);
            assertEquals(authDb.getToken(), response.getData().getToken());
            assertEquals(authDb.getTokenExp(), response.getData().getTokenExp());
        });
    }

    @Test
    void testResetPasswordSuccess() throws Exception{
        ResetRequest request = new ResetRequest();
        request.setUsername("atri10");

        mockMvc.perform(
                post("/api/reset-password")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String>response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertEquals("OKE", response.getData());
        });
    }
    @Test
    void testResetPasswordBad() throws Exception{
        ResetRequest request = new ResetRequest();
        request.setUsername("");

        mockMvc.perform(
                post("/api/reset-password")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String>response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testChangePasswordSuccess() throws Exception{
        Auth auth = new Auth();
        auth.setPassword(BCrypt.hashpw("654321", BCrypt.gensalt()));
        auth.setUsername("atri10");
        auth.setStatus(1);
        auth.setRole(1);
        authRepository.save(auth);

        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setUsername("atri10");
        resetPassword.setId("123456");
        resetPassword.setIdExp(System.currentTimeMillis()+100000000);
        resetPasswordRepository.save(resetPassword);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setUsername("atri10");
        changePasswordRequest.setPassword("123456");
        changePasswordRequest.setIdReset("123456");
        changePasswordRequest.setIdResetExp(System.currentTimeMillis()+100000000);

        mockMvc.perform(
                post("/api/change-password")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest))
        ).andExpect(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String>response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            Auth auth1 = authRepository.findById(changePasswordRequest.getUsername()).orElse(null);
            assert auth1 != null;
            assertTrue(BCrypt.checkpw("123456", auth1.getPassword()));
        });
    }

    @Test
    void testChangePasswordIDWrong() throws Exception{
        Auth auth = new Auth();
        auth.setPassword(BCrypt.hashpw("654321", BCrypt.gensalt()));
        auth.setUsername("atri10");
        auth.setStatus(1);
        auth.setRole(1);
        authRepository.save(auth);

        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setUsername("atri10");
        resetPassword.setId("123456");
        resetPassword.setIdExp(System.currentTimeMillis()+100000000);
        resetPasswordRepository.save(resetPassword);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setUsername("atri10");
        changePasswordRequest.setPassword("123456");
        changePasswordRequest.setIdReset("123456rer");
        changePasswordRequest.setIdResetExp(System.currentTimeMillis()+100000000);

        mockMvc.perform(
                post("/api/change-password")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String>response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
            assertEquals("ID ini sudah tidak berlaku", response.getErrors());
        });
    }

    @Test
    void testChangePasswordWrongUser() throws Exception{
        Auth auth = new Auth();
        auth.setPassword(BCrypt.hashpw("654321", BCrypt.gensalt()));
        auth.setUsername("atri10");
        auth.setStatus(1);
        auth.setRole(1);
        authRepository.save(auth);

        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setUsername("atri10");
        resetPassword.setId("123456");
        resetPassword.setIdExp(System.currentTimeMillis()+100000000);
        resetPasswordRepository.save(resetPassword);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setUsername("atri1000");
        changePasswordRequest.setPassword("123456");
        changePasswordRequest.setIdReset("123456");
        changePasswordRequest.setIdResetExp(System.currentTimeMillis()+100000000);

        mockMvc.perform(
                post("/api/change-password")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String>response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
            assertEquals("ID ini sudah tidak berlaku", response.getErrors());
        });
    }

    @Test
    void testChangePasswordNotFoundUser() throws Exception{
        Auth auth = new Auth();
        auth.setPassword(BCrypt.hashpw("654321", BCrypt.gensalt()));
        auth.setUsername("atri10000");
        auth.setStatus(1);
        auth.setRole(1);
        authRepository.save(auth);

        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setUsername("atri10");
        resetPassword.setId("123456");
        resetPassword.setIdExp(System.currentTimeMillis()+100000000);
        resetPasswordRepository.save(resetPassword);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setUsername("atri10");
        changePasswordRequest.setPassword("123456");
        changePasswordRequest.setIdReset("123456");
        changePasswordRequest.setIdResetExp(System.currentTimeMillis()+100000000);

        mockMvc.perform(
                post("/api/change-password")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String>response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
            assertEquals("Username not Found", response.getErrors());
        });
    }
    @Test
    void testChangePasswordExpiredId() throws Exception{
        Auth auth = new Auth();
        auth.setPassword(BCrypt.hashpw("654321", BCrypt.gensalt()));
        auth.setUsername("atri10");
        auth.setStatus(1);
        auth.setRole(1);
        authRepository.save(auth);

        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setUsername("atri10");
        resetPassword.setId("123456");
        resetPassword.setIdExp(System.currentTimeMillis()+100000000);
        resetPasswordRepository.save(resetPassword);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setUsername("atri10");
        changePasswordRequest.setPassword("123456");
        changePasswordRequest.setIdReset("123456");
        changePasswordRequest.setIdResetExp(System.currentTimeMillis()-100000000);

        mockMvc.perform(
                post("/api/change-password")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String>response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
            assertEquals("ID ini sudah tidak berlaku", response.getErrors());
        });
    }
}
