package com.kiran.user.management.controller;


import com.kiran.user.management.exception.ResourceNotFoundException;
import com.kiran.user.management.exception.UserNotAuthenticatedException;
import com.kiran.user.management.model.User;
import com.kiran.user.management.service.TokenValidator;
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
    private final TokenValidator tokenValidator;

    @Autowired
    public UserController(UserService userService, TokenValidator tokenValidator) {
        this.userService = userService;
        this.tokenValidator = tokenValidator;
    }

    /**
     * API mapping to get all uses
     *
     * @return list of all users
     */
    @GetMapping("/users")
    public List<User> getAllUsers(@RequestHeader (name="Authorization") String jwtToken) throws UserNotAuthenticatedException {
        validateAuthToken(jwtToken);
        return userService.findAll();
    }

    /**
     * Validates given token
     * @param jwtToken token to be validated
     * @throws UserNotAuthenticatedException throws user defined exception
     */
    public void validateAuthToken(String jwtToken) throws UserNotAuthenticatedException {
         final String PREFIX = "Bearer ";
        String authToken = jwtToken.replace(PREFIX, "");

        if (!tokenValidator.validate(authToken)) {
            throw new UserNotAuthenticatedException("Invalid or expired user token");
        }
    }

    /**
     * retrieves the user associated with userId
     * @param userId  used to get the user
     * @return User represents user associated with userID
     * @throws ResourceNotFoundException  throws when  resource not found
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId,@RequestHeader (name="Authorization") String jwtToken) throws ResourceNotFoundException, UserNotAuthenticatedException {
        validateAuthToken(jwtToken);
        User user = userService.findById(userId);

        return ResponseEntity.ok().body(user);
    }

    /**
     * creates a new user
     * @param user body with user details
     * @return  user created user by using user details
     */
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user, @RequestHeader (name="Authorization") String jwtToken) {
      //  validateAuthToken(jwtToken);
        return userService.save(user);
    }

    /**
     * Updates user details  associated with id .Mail id will not be updated
     *
     * @param userId of user to update the details
     * @param userDetails details to be updated
     * @return response code updated user details error if id didn't found
     * @throws ResourceNotFoundException user defined exception
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @RequestBody User userDetails, @RequestHeader (name="Authorization") String jwtToken) throws ResourceNotFoundException, UserNotAuthenticatedException {
        validateAuthToken(jwtToken);
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
     * @param userId id of the user to be deleted
     * @throws ResourceNotFoundException user defined exception
     */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable(value = "id") Long userId, @RequestHeader (name="Authorization") String jwtToken) throws ResourceNotFoundException, UserNotAuthenticatedException {
        validateAuthToken(jwtToken);
        userService.delete(userId);
    }
}
