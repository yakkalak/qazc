package com.kiran.user.management.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    private long id;
    private String name;
    private String password;
    private String emailId;
    private Instant lastLoginTime;

    public User() {
        this.lastLoginTime = Instant.now();
    }

    public User(String name, String password, String emailId) {
        this.name = name;
        this.password = password;
        this.emailId = emailId;
        this.lastLoginTime = Instant.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Column(name = "email_address", nullable = false)
    public String getEmailId() {
        return emailId;
    }

    public User setEmailId(String emailId) {
        this.emailId = emailId;
        return this;
    }

    @Column(name = "last_login", nullable = true)
    public Instant getLastLoginTime() {
        return lastLoginTime;
    }

    public User setLastLoginTime(Instant lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", emailId='" + emailId + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                '}';
    }
}
