package passwordApp.service;

import passwordApp.dtos.request.PasswordUpdateRequest;
import passwordApp.dtos.response.passwordUpdateResponse;
import passwordApp.model.data.PasswordEntry;
import passwordApp.model.data.User;
import passwordApp.repository.PasswordEntryRepository;
import passwordApp.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {

    private final PasswordEntryRepository passwordEntryRepository;

    @Autowired
    public PasswordService(PasswordEntryRepository passwordEntryRepository) {
        this.passwordEntryRepository = passwordEntryRepository;
    }

    public List<PasswordEntry> getAllPasswords(User user) {
        return passwordEntryRepository.findByUser(user);
    }

public PasswordEntry addPassword(PasswordEntry entry) {
    if (entry == null || entry.getEncryptedPassword() == null) {
        throw new IllegalArgumentException("Invalid password entry");
    }

    String encrypted = EncryptionUtil.encrypt(entry.getEncryptedPassword());
    entry.setEncryptedPassword(encrypted);

    return passwordEntryRepository.save(entry);
}

    public void deletePassword(long id) {
        if (!passwordEntryRepository.existsById(id)) {
            throw new RuntimeException("Password entry not found for ID: " + id);
        }
        try {
            passwordEntryRepository.deleteById(id);
            System.out.println("Password entry deleted for ID: " + id);
        } catch (Exception e) {
            System.out.println("Error deleting password entry: " + e.getMessage());
            throw new RuntimeException("Failed to delete password entry", e);
        }
    }

public passwordUpdateResponse updatePassword(PasswordUpdateRequest request) {
    PasswordEntry existingEntry = passwordEntryRepository.findById(request.getEntryId())
            .orElseThrow(() -> new RuntimeException("Password entry not found"));

    existingEntry.setSiteUrl(request.getSiteName());
    existingEntry.setUsername(request.getUsername());

    String encrypted = EncryptionUtil.encrypt(request.getRawPassword());
    existingEntry.setEncryptedPassword(encrypted);

    PasswordEntry saved = passwordEntryRepository.save(existingEntry);
    return new passwordUpdateResponse(saved);

}

public void savePassword(PasswordEntry entry) {
    String encrypted = EncryptionUtil.encrypt(entry.getEncryptedPassword());
    entry.setEncryptedPassword(encrypted);
    passwordEntryRepository.save(entry);
}
}