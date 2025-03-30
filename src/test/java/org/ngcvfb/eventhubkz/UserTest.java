package org.ngcvfb.eventhubkz;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ngcvfb.eventhubkz.Models.Role;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Repository.UserRepository;
import org.ngcvfb.eventhubkz.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private UserModel userTest;

    @BeforeEach
    public void setUp() {
        // Попробуем найти пользователя по email. Если не найден, создадим его.
        userTest = userRepository.findByEmail("test@example.com").orElseGet(() -> {
            UserModel user = UserModel.builder()
                    .username("testuser")
                    .email("test@example.com")
                    .password("secret")
                    .role(Role.USER)
                    .enabled(true)
                    .build();
            return userRepository.save(user);
        });
    }

    @Test
    void getUserById(){
        UserModel user = userService.getUserById(userTest.getId());
        assertNotNull(user, "user should not be null");
        assertEquals("test@example.com", user.getEmail(), "user email does not match");
    }

    @Test
    void getUserByEmail(){
        UserModel user = userService.getUserByEmail(userTest.getEmail());
        assertNotNull(user, "user should not be null");
        assertEquals("test@example.com", user.getEmail(), "user email does not match");
    }
    @Test
    void updateUser(){
        UserModel user = userService.getUserById(userTest.getId());
        user.setEmail("updated@example.com");
        userRepository.save(user);
        user = userService.getUserById(userTest.getId());
        assertEquals("updated@example.com", user.getEmail(), "user email does not match");
    }
    @Test
    void deleteUser(){
        UserModel user = userService.getUserById(userTest.getId());
        userRepository.delete(user);
        user = userService.getUserById(userTest.getId());
        assertNull(user, "user should not be null");
    }

    @Test
    public void testGetAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        assertFalse(users.isEmpty(), "Список пользователей не должен быть пустым");
        // Можно проверить, что список содержит testUser
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("test@example.com")));
    }

}

