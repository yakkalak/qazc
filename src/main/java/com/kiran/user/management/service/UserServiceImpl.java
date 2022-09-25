package com.kiran.user.management.service;

import com.kiran.user.management.exception.ResourceNotFoundException;
import com.kiran.user.management.model.User;
import com.kiran.user.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long userId) throws ResourceNotFoundException {
        User userToBeDeleted = findById(userId);

        userRepository.delete(userToBeDeleted);
    }

    @Override
    public User findByEmailIdAndPassword(String emailId, String password) throws ResourceNotFoundException {
        return userRepository.findByEmailIdAndPassword(emailId, password)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this email id :: " + emailId));
    }
}
