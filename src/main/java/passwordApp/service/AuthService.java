package passwordApp.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import passwordApp.dtos.request.UserRequestDto;
import passwordApp.dtos.response.UserResponseDto;
import passwordApp.model.data.User;
import passwordApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import passwordApp.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponseDto register(UserRequestDto userRequestDto) {
        if (userRequestDto == null ||
                userRequestDto.getUsername() == null ||
                userRequestDto.getEmail() == null ||
                userRequestDto.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing registration fields");
        }

        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        User user = new User();
        user.setUsername(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userRequestDto.getPassword()));

        User savedUser = userRepository.save(user);

        UserResponseDto response = new UserResponseDto();
        response.setUserId(savedUser.getUserId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        return response;

    }

    public UserResponseDto login(String username, String password) {
        if (username == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password required");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        //User savedUser = userRepository.save(user);

        String jwtToken = jwtUtil.generateToken(user);

        UserResponseDto response = new UserResponseDto();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setToken(jwtToken);
        return response;
    }
}