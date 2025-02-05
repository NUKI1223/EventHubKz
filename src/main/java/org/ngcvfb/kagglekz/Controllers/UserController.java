package org.ngcvfb.kagglekz.Controllers;

import org.ngcvfb.kagglekz.DTO.EventDTO;
import org.ngcvfb.kagglekz.DTO.EventRequestDTO;
import org.ngcvfb.kagglekz.DTO.UserDTO;
import org.ngcvfb.kagglekz.Models.EventRequest;
import org.ngcvfb.kagglekz.Models.Role;
import org.ngcvfb.kagglekz.Models.UserModel;
import org.ngcvfb.kagglekz.Services.EventRequestService;
import org.ngcvfb.kagglekz.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPassword(userDTO.getPassword());
        existingUser.setDescription(userDTO.getDescription());
        existingUser.setAvatarUrl(userDTO.getAvatarUrl());
        existingUser.setContacts(userDTO.getContacts());

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
