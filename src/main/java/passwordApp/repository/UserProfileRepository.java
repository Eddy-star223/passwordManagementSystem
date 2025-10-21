package passwordApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passwordApp.model.data.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser_UserId(Long userId);
}