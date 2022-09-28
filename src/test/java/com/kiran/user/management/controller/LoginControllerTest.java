package com.kiran.user.management.controller;
import com.kiran.user.management.model.User;
import com.kiran.user.management.service.TokenGenerator;
import com.kiran.user.management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {
    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(loginController)
                .build();
    }
    @Test
    void login() throws Exception {
        User user = createNewUser();
        when(userService.findByEmailIdAndPassword(user.getEmailId(),user.getPassword())).thenReturn(user);

        mockMvc.perform(post("/login" )
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"emailId\":\"test\",\"password\":\"passcode\"}")).andExpect(status().isOk());
        verify(userService).findByEmailIdAndPassword(user.getEmailId(),user.getPassword());
    }

    private User createNewUser() {
        return new User()
                .setId(123456)
                .setName("Test")
                .setPassword("passcode")
                .setEmailId("test")
                .setLastLoginTime(Instant.now());
    }
}
