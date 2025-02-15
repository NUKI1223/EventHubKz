package org.ngcvfb.eventhubkz.Controllers;

import org.ngcvfb.eventhubkz.DTO.EventRequestDTO;
import org.ngcvfb.eventhubkz.DTO.UserDTO;
import org.ngcvfb.eventhubkz.Models.Role;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Services.EventRequestService;
import org.ngcvfb.eventhubkz.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final EventRequestService eventRequestService;

    public UserController(UserService userService, EventRequestService eventRequestService) {
        this.userService = userService;
        this.eventRequestService = eventRequestService;
    }

    // Получить всех пользователей
    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/me")
    public ResponseEntity<UserModel> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel currentUser = (UserModel) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    // Получить пользователя по ID
    @GetMapping("/id/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) {
        UserModel user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // Получить пользователя по email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserModel> getUserByEmail(@PathVariable String email) {
        UserModel user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // Создать нового пользователя
    @PostMapping
    public ResponseEntity<UserModel> createUser(@RequestBody UserDTO userDTO) {
        UserModel user = new UserModel();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(Role.valueOf(userDTO.getRole().toUpperCase()));
        user.setAvatarUrl(userDTO.getAvatarUrl());
        user.setContacts(userDTO.getContacts());
        UserModel createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
    @PostMapping("/eventRequest")
    public ResponseEntity<EventRequestDTO> createEventRequest(
            @RequestBody EventRequestDTO eventRequestDTO,
            @RequestParam String requesterEmail) {
        EventRequestDTO createdRequest = eventRequestService.createEventRequest(eventRequestDTO, requesterEmail);
        return ResponseEntity.ok(createdRequest);
    }

    // Обновить существующего пользователя
    @PutMapping("/id/{id}")
    public ResponseEntity<UserModel> updateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDTO userDTO
    ) {
        UserModel existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Обновление полей пользователя
        if (userDTO.getUsername() != null) {
            existingUser.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            existingUser.setPassword(userDTO.getPassword());
        }
        if (userDTO.getRole() != null) {
            if (userDTO.getRole().equals("ADMIN")) {
                existingUser.setRole(Role.ADMIN);
            } else if (userDTO.getRole().equals("USER")) {
                existingUser.setRole(Role.USER);
            }
        }
        if (userDTO.getContacts() != null) {
            existingUser.setContacts(userDTO.getContacts());
        }
        if (userDTO.getDescription() != null) {
            existingUser.setDescription(userDTO.getDescription());
        }

        UserModel updatedUser = userService.updateUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

        // Удалить пользователя по ID
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
