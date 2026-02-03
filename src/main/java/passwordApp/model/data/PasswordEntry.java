package passwordApp.model.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
public class PasswordEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long entryId;

    @NotBlank
    @Column(nullable = false)
    private String siteUrl;

    @NotBlank
    @Column(nullable = false)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String encryptedPassword;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public @NotBlank String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(@NotBlank String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @NotBlank String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(@NotBlank String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasswordEntry)) return false;
        PasswordEntry that = (PasswordEntry) o;
        return entryId == that.entryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryId);
    }

    @Override
    public String toString() {
        return "PasswordEntry{" +
                "siteName='" + siteUrl + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}