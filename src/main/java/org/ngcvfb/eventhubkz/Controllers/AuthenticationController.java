package org.ngcvfb.eventhubkz.Controllers;

import org.ngcvfb.eventhubkz.DTO.LoginUserDTO;
import org.ngcvfb.eventhubkz.DTO.UserDTO;
import org.ngcvfb.eventhubkz.DTO.VerifyUserDto;
import org.ngcvfb.eventhubkz.Models.LoginResponse;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Services.AuthenticationService;
import org.ngcvfb.eventhubkz.Services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    public AuthenticationController(final JwtService jwtService, final AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }
    @PostMapping("/signup")
    public ResponseEntity<UserModel> register(@RequestBody UserDTO registerUserDto) {
        UserModel registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDto){
        UserModel authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
