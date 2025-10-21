package passwordApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import passwordApp.dtos.request.UserRequestDto;
import passwordApp.dtos.response.UserResponseDto;
import passwordApp.service.AuthService;

import jakarta.validation.Valid;
import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1:5501")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto savedUser = authService.register(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody Map<String, String> user) {
        System.out.println("🔐 Login request received: " + user);
        String username = user.get("username");
        String password = user.get("password");

        if (username == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password required");
        }

        UserResponseDto dto = authService.login(username, password);
        return ResponseEntity.ok(dto);
    }
}