package com.kiran.user.management.service;

import com.kiran.user.management.exception.ResourceNotFoundException;
import com.kiran.user.management.model.User;
import com.kiran.user.management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = createUser();
    }

    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        List<User> users = userService.findAll();

        assertThat("number of users: ", users, hasSize(1));
        assertThat("users: ", users, hasItem(testUser));
        verify(userRepository).findAll();
    }

    @Test
    void findById() throws ResourceNotFoundException {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        User user = userService.findById(testUser.getId());

        assertThat("retrieved user: ", user, is(testUser));
        verify(userRepository).findById(testUser.getId());
    }

    @Test
    void findById_userNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.findById(testUser.getId()));

        assertThat("exception message: ", exception.getMessage(), containsString("User not found"));
        verify(userRepository).findById(testUser.getId());
    }

    @Test
    void save() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User user = userService.save(testUser);

        assertThat("updated user: ", user, is(testUser));
        verify(userRepository).save(testUser);
    }

    @Test
    void delete() throws ResourceNotFoundException {
        long userId = testUser.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        userService.delete(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).delete(testUser);
    }

    @Test
    void findByEmailIdAndPassword() throws ResourceNotFoundException {
        when(userRepository.findByEmailIdAndPassword(testUser.getEmailId(), testUser.getPassword())).thenReturn(Optional.of(testUser));

        User user = userService.findByEmailIdAndPassword(testUser.getEmailId(), testUser.getPassword());

        assertThat("retrieved user: ", user, is(testUser));
        verify(userRepository).findByEmailIdAndPassword(testUser.getEmailId(), testUser.getPassword());
    }

    @Test
    void findByEmailIdAndPassword_userNotFound() {
        when(userRepository.findByEmailIdAndPassword(testUser.getEmailId(), testUser.getPassword())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.findByEmailIdAndPassword(testUser.getEmailId(), testUser.getPassword()));

        assertThat("exception message: ", exception.getMessage(), containsString("User not found"));
        verify(userRepository).findByEmailIdAndPassword(testUser.getEmailId(), testUser.getPassword());
    }

    private User createUser() {
        return new User()
                .setId(123456)
                .setName("Test")
                .setPassword("passcode")
                .setEmailId("test@testing.com")
                .setLastLoginTime(Instant.now());
    }
}