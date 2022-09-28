package com.kiran.user.management.controller;

import com.kiran.user.management.exception.ResourceNotFoundException;
import com.kiran.user.management.model.User;
import com.kiran.user.management.service.TokenGenerator;
import com.kiran.user.management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private UserService userService;


    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/user-management/users").header(HttpHeaders.AUTHORIZATION,""))
                .andExpect(status().isOk());

        verify(userService).findAll();
    }

    @Test
    void getUserById() throws Exception {
        User user = createTestUser();
        when(userService.findById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/user-management/users/" + user.getId()))
                .andExpect(status().isOk());

        verify(userService).findById(user.getId());
    }

    @Test
    void getUserById_userNotFound() throws Exception {
        when(userService.findById(anyLong())).thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/user-management/users/2"))
                .andExpect(status().isNotFound());

        verify(userService).findById(2L);
    }

    @Test
    void createUser() throws Exception {
        User user = createTestUser();
        when(userService.save(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/user-management/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"100\",\"name\":\"test\",\"emailId\":\"test\",\"password\":\"passcode\"}"))
                .andExpect(status().isOk());
               // .andExpect(status().isOk());
        verify(userService).save(user);
    }

    @Test
    void updateUser() throws Exception {
        User user = createTestUser();
        when(userService.findById(user.getId())).thenReturn(user);
        mockMvc.perform(put("/user-management/users/2"))
                .andExpect(status().isOk());
        verify(userService).save(user);
    }

    @Test
    void deleteUser() throws Exception {
        String jwtToken = "";
        when(tokenGenerator.generateJsonWebToken()).thenReturn(jwtToken);
        mockMvc.perform(delete("/user-management/users/2")
                                .contentType(jwtToken)
                                .content(jwtToken)
                                .header(HttpHeaders.AUTHORIZATION, jwtToken));
                //.andExpect(status().isOk());

        verify(userService).delete(2L);
    }


    private User createTestUser() {
        return new User()
                .setId(123456)
                .setName("Test")
                .setPassword("passcode")
                .setEmailId("test@testing.com")
                .setLastLoginTime(Instant.now());
    }
}