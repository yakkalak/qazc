package com.kiran.user.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import com.kiran.user.management.model.User;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserManagementControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${local.server.port}")
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void contextLoads() {

    }

    @Test
    public void testGetAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/users",
                HttpMethod.GET, entity, String.class);
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetUserById() {
        User user = restTemplate.getForObject(getRootUrl() + "/users/44", User.class);
        System.out.println(user.getName());
        assertNotNull(user);
    }

    @Test
    public void testUserLogin() {
        User user = restTemplate.getForObject(getRootUrl() + "/users/login/kiran@testng.com/test", User.class);
        System.out.println(user.getName());
        assertNotNull(user);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("kiran");
        user.setEmailId("kiran@testng.com");
        user.setPassword("test");
        ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/users", user, User.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdateUser() {
        int id = 1;
        User user = restTemplate.getForObject(getRootUrl() + "/users/" + id, User.class);
        user.setName("kirankumar");
        restTemplate.put(getRootUrl() + "/users/" + id, user);
        User updatedUSer = restTemplate.getForObject(getRootUrl() + "/users/" + id, User.class);
        assertNotNull(updatedUSer);
    }

    @Test
    public void testDeleteUser() {
        int id = 2;
        User user = restTemplate.getForObject(getRootUrl() + "/employees/" + id, User.class);
        assertNotNull(user);
        restTemplate.delete(getRootUrl() + "/users/" + id);
        try {
            user = restTemplate.getForObject(getRootUrl() + "/users/" + id, User.class);
        } catch (final HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
}
