package com.kiran.user.management.controller;


import com.kiran.user.management.exception.ResourceNotFoundException;
import com.kiran.user.management.model.LoginUserDto;
import com.kiran.user.management.model.User;
import com.kiran.user.management.repository.UserRepository;
import com.kiran.user.management.service.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.*;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user-management")
public class UserController {
    private final TokenGenerator tokenGenerator;
    private final UserRepository userRepository;

    @Autowired
    public UserController(TokenGenerator tokenGenerator, UserRepository userRepository) {
        this.tokenGenerator = tokenGenerator;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                                   @Valid @RequestBody User userDetails) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        user.setEmailId(userDetails.getEmailId());
        user.setPassword(userDetails.getPassword());
        user.setName(userDetails.getName());
        user.setLastLoginTime(userDetails.getLastLoginTime());

        final User updatedUser = userRepository.save(user);

        return ResponseEntity.ok(updatedUser);
    }

   @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginUserDto user)
            throws ResourceNotFoundException {
        User loggedInUser = userRepository.findByEmailIdAndPassword(user.getEmailId(),user.getPassword())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + user.getEmailId()));

        loggedInUser.setLastLoginTime(Instant.now());
        final User updatedUser = userRepository.save(loggedInUser);

        return tokenGenerator.generateJsonWebToken();
    }

}
