package passwordApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passwordApp.model.data.PasswordEntry;
import passwordApp.model.data.User;

import java.util.List;
import java.util.Optional;

public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {

    List<PasswordEntry> findByUser(User user);

    Optional<PasswordEntry> findByUserAndSiteName(User user, String siteName);

    List<PasswordEntry> findByUserAndSiteNameContainingIgnoreCase(User user, String keyword);

    void deleteByUser(User user);
}