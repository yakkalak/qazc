package com.kiran.user.management.controller;


import com.kiran.user.management.exception.ResourceNotFoundException;
import com.kiran.user.management.model.User;
import com.kiran.user.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user-management")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * API mapping to get all uses
     *
     * @return list of all users
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    /**
     * retrieves the user associated with userId
     *
     * @param userId used to get the user
     * @return User represents user associated with userID
     * @throws ResourceNotFoundException throws when  resource not found
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        User user = userService.findById(userId);

        return ResponseEntity.ok().body(user);
    }

    /**
     * creates a new user
     *
     * @param user body with user details
     * @return user created user by using user details
     */
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userService.save(user);
    }

    /**
     * Updates user details  associated with id .Mail id will not be updated
     *
     * @param userId      of user to update the details
     * @param userDetails details to be updated
     * @return response code updated user details error if id didn't found
     * @throws ResourceNotFoundException user defined exception
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @RequestBody User userDetails) throws ResourceNotFoundException {
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
     * @throws ResourceNotFoundException user defined exception
     */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        userService.delete(userId);
    }
}
