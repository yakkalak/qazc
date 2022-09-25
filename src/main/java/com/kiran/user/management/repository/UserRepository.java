package com.kiran.user.management.repository;

import com.kiran.user.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmailIdAndPassword(String emailId, String password);


}