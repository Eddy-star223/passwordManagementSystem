package passwordApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passwordApp.model.data.PasswordEntry;
import passwordApp.model.data.User;

import java.util.List;
import java.util.Optional;

public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {

    List<PasswordEntry> findByUser(User user);

    Optional<PasswordEntry> findByUserAndSiteUrl(User user, String siteUrl);

    List<PasswordEntry> findByUserAndSiteUrlContainingIgnoreCase(User user, String keyword);

    void deleteByUser(User user);
}