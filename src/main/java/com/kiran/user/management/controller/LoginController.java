package com.kiran.user.management.controller;

import com.kiran.user.management.exception.ResourceNotFoundException;
import com.kiran.user.management.model.LoginUserDto;
import com.kiran.user.management.model.User;
import com.kiran.user.management.service.TokenGenerator;
import com.kiran.user.management.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;

@RestController
public class LoginController {

    /**
     * Token generator
     */
    private final TokenGenerator tokenGenerator;
    private final UserService userService;

    public LoginController(TokenGenerator tokenGenerator, UserService userService) {
        this.tokenGenerator = tokenGenerator;
        this.userService = userService;
    }

    /**
     * Handles login request. Logs in if user supplied passord
     * and username matches. Updates last login time ones authenticated.
     * @return a Token after successfull validation
     * @throws ResourceNotFoundException throws customized exception
     */
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginUserDto user) throws ResourceNotFoundException {
        User loggedInUser = userService.findByEmailIdAndPassword(user.getEmailId(), user.getPassword());
        loggedInUser.setLastLoginTime(Instant.now());
        userService.save(loggedInUser);
        return tokenGenerator.generateJsonWebToken();
    }
}
