package passwordApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import passwordApp.dtos.request.PasswordUpdateRequest;
import passwordApp.dtos.response.PasswordEntryDto;
import passwordApp.dtos.response.passwordUpdateResponse;
import passwordApp.model.data.PasswordEntry;
import passwordApp.model.data.User;
import passwordApp.model.data.UserProfile;
import passwordApp.repository.UserProfileRepository;
import passwordApp.repository.UserRepository;
import passwordApp.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import passwordApp.utils.EncryptionUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://127.0.0.1:5501")
@CrossOrigin(origins = "https://absorptive-lakeesha-semiconically.ngrok-free.dev")
@RestController
@RequestMapping("/api/passwordSystem")
public class PasswordController {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public PasswordController(UserRepository userRepository, PasswordService passwordService,  UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.userProfileRepository = userProfileRepository;
    }

@PostMapping
public ResponseEntity<?> addPassword(@RequestBody PasswordEntryDto dto) {
    if (dto.getUserId() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is missing");
    }

    User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    PasswordEntry entry = new PasswordEntry();
    entry.setSiteUrl(dto.getSiteUrl());
    entry.setUsername(dto.getUsername());

    String encrypted = EncryptionUtil.encrypt(dto.getPassword());
    entry.setEncryptedPassword(encrypted);

    entry.setUser(user);
    passwordService.savePassword(entry);

    return ResponseEntity.status(HttpStatus.CREATED).build();
}

@GetMapping("/{userId}")
public ResponseEntity<List<PasswordEntryDto>> getPasswords(@PathVariable("userId") long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    List<PasswordEntry> entries = passwordService.getAllPasswords(user);

    List<PasswordEntryDto> dtos = entries.stream().map(entry -> {
        PasswordEntryDto dto = new PasswordEntryDto();
        dto.setEntryId(entry.getEntryId());
        dto.setSiteUrl(entry.getSiteUrl());
        dto.setUsername(entry.getUsername());
        dto.setUserId(entry.getUser().getUserId());

        String decrypted = EncryptionUtil.decrypt(entry.getEncryptedPassword());
        dto.setPassword(decrypted);

        return dto;
    }).collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
}
    @PutMapping("/{entryId}")
    public ResponseEntity<passwordUpdateResponse> updatePassword(
            @PathVariable(name = "entryId") Long entryId,
            @RequestBody PasswordUpdateRequest request) {

        request.setEntryId(entryId);
        passwordUpdateResponse updated = passwordService.updatePassword(request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassword(@PathVariable("id") long id) {
        System.out.println("DELETE request received for ID: " + id);
        passwordService.deletePassword(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/profile/update")
    public ResponseEntity<?> updateProfile(
            @RequestParam("userId") Long userId,
            @RequestParam("notes") String notes,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                System.out.println("User not found: " + userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            User user = optionalUser.get();
            UserProfile profile = userProfileRepository.findById(userId).orElse(new UserProfile());
            profile.setUser(user);
            profile.setNotes(notes);

            if (imageFile != null && !imageFile.isEmpty()) {
                profile.setProfileImage(imageFile.getBytes());
            }

            userProfileRepository.save(profile);
            System.out.println("Profile saved for user: " + userId);

            return ResponseEntity.ok("Profile updated");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @GetMapping("/profile/image/{userId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long userId) {
        Optional<UserProfile> optionalProfile = userProfileRepository.findById(userId);
        if (optionalProfile.isEmpty() || optionalProfile.get().getProfileImage() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageData = optionalProfile.get().getProfileImage();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageData);
    }
}



