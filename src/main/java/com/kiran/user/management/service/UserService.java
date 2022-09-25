package com.kiran.user.management.service;

import com.kiran.user.management.exception.ResourceNotFoundException;
import com.kiran.user.management.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Long userId) throws ResourceNotFoundException;

    User save(User user);

    void delete(Long userId) throws ResourceNotFoundException;

    User findByEmailIdAndPassword(String emailId, String password) throws ResourceNotFoundException;
}
