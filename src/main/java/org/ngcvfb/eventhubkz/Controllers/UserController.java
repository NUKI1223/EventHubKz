package org.ngcvfb.eventhubkz.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ngcvfb.eventhubkz.DTO.EventRequestDTO;
import org.ngcvfb.eventhubkz.DTO.UserDTO;
import org.ngcvfb.eventhubkz.Models.Role;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Services.EventRequestService;
import org.ngcvfb.eventhubkz.Services.S3Service;
import org.ngcvfb.eventhubkz.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;
    private final EventRequestService eventRequestService;
    private final S3Service s3Service;


    public UserController(UserService userService, EventRequestService eventRequestService, S3Service s3Service) {
        this.userService = userService;
        this.eventRequestService = eventRequestService;
        this.s3Service = s3Service;
    }


    @Operation(summary = "Getting all users")
    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @Operation(summary = "Getting authenticated user")
    @GetMapping("/me")
    public ResponseEntity<UserModel> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel currentUser = (UserModel) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @Operation(summary = "Getting user by id")
    @GetMapping("/id/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) {
        UserModel user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Getting user by username")
    @GetMapping("/{username}")
    public ResponseEntity<UserModel> getUserByUsername(@PathVariable String username) {
        UserModel user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Getting user by email")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserModel> getUserByEmail(@PathVariable String email) {
        UserModel user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Creating an eventRequest for ADMIN")
    @PostMapping(value = "/eventRequest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventRequestDTO> createEventRequest(
            @RequestPart("eventRequest") EventRequestDTO eventRequestDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                File tempFile = File.createTempFile("eventrequest-", file.getOriginalFilename());
                file.transferTo(tempFile);
                String key = "event-requests/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
                String imageUrl = s3Service.uploadFile(key, tempFile);
                tempFile.delete();
                eventRequestDto.setMainImageUrl(imageUrl);
            }
            EventRequestDTO createdRequest = eventRequestService.createEventRequest(eventRequestDto);
            return ResponseEntity.ok(createdRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }


    }

    @Operation(summary = "Updating user")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserModel> updateUser(
            @RequestPart("user") UserDTO userDTO,
            @RequestPart(value = "file",  required = false) MultipartFile file

    ) {
        try {
            if (file != null && !file.isEmpty()) {
                File tempFile = File.createTempFile("profile-", file.getOriginalFilename());
                file.transferTo(tempFile);
                String key = "users/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
                String fileUrl = s3Service.uploadFile(key, tempFile);
                tempFile.delete();
                userDTO.setAvatarUrl(fileUrl);
            }
            userService.updateUser(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
        UserModel updatedUser = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Deleting user by id")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
