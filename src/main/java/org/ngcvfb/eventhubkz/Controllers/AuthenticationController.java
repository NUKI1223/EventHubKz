package org.ngcvfb.eventhubkz.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ngcvfb.eventhubkz.DTO.LoginUserDTO;
import org.ngcvfb.eventhubkz.DTO.UserDTO;
import org.ngcvfb.eventhubkz.DTO.VerifyUserDto;
import org.ngcvfb.eventhubkz.Models.LoginResponse;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Services.AuthenticationService;
import org.ngcvfb.eventhubkz.Services.JwtService;
import org.ngcvfb.eventhubkz.Services.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Tag(name = "Authentication", description = "Authentication API with JWT token")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final S3Service s3Service;

    public AuthenticationController(final JwtService jwtService, final AuthenticationService authenticationService, S3Service s3Service) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.s3Service = s3Service;
    }
    @Operation(summary = "Registration")
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(@RequestPart("user") UserDTO registerUserDto,
                                      @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            if (file != null && !file.isEmpty()) {
                File tempFile = File.createTempFile("profile-", file.getOriginalFilename());
                file.transferTo(tempFile);
                String key = "users/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
                String fileUrl = s3Service.uploadFile(key, tempFile);
                tempFile.delete();
                registerUserDto.setAvatarUrl(fileUrl);
            }
            if (registerUserDto.getPassword() == null ||
                    registerUserDto.getUsername() == null ||
                    registerUserDto.getPassword().isEmpty() ||
                    registerUserDto.getEmail().isEmpty()
            ) {
                return ResponseEntity.badRequest().body("Please fill all the required fields");
            }
            if (registerUserDto.getPassword().length() < 8){
                return ResponseEntity.badRequest().body("Your password must have at least 8 characters");
            }

            UserModel registeredUser = authenticationService.signup(registerUserDto);
            return ResponseEntity.ok("Verification code sent");
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }

    }

    @Operation(summary = "Authentication")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDto){
        UserModel authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser, authenticatedUser.getRole());
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @Operation(summary = "Verifying user email by verification code")
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        authenticationService.verifyUser(verifyUserDto);
        return ResponseEntity.ok("Account verified successfully");

    }


    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        authenticationService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent");

    }
}
