package com.kiran.user.management.controller;


import com.kiran.user.management.exception.ResourceNotFoundException;
import com.kiran.user.management.model.LoginUserDto;
import com.kiran.user.management.model.User;
import com.kiran.user.management.service.TokenGenerator;
import com.kiran.user.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user-management")
public class UserController {
    private final TokenGenerator tokenGenerator;
    private final UserService userService;

    @Autowired
    public UserController(TokenGenerator tokenGenerator, UserService userService) {
        this.tokenGenerator = tokenGenerator;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userService.findById(userId);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                           @Valid @RequestBody User userDetails) throws ResourceNotFoundException {
        User user = userService.findById(userId);

        // update all except `emailId`
        user.setPassword(userDetails.getPassword());
        user.setName(userDetails.getName());
        user.setLastLoginTime(userDetails.getLastLoginTime());

        final User updatedUser = userService.save(user);

        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Deletes the user associated with the given id.
     * Returns an empty response with 200 OK response if deleted successfully, else an error code
     *
     * @param userId id of the user to be deleted
     * @throws ResourceNotFoundException
     */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        userService.delete(userId);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginUserDto user) throws ResourceNotFoundException {
        User loggedInUser = userService.findByEmailIdAndPassword(user.getEmailId(), user.getPassword());

        loggedInUser.setLastLoginTime(Instant.now());
        userService.save(loggedInUser);

        return tokenGenerator.generateJsonWebToken();
    }

}
