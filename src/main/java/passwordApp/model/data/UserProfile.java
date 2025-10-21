package passwordApp.model.data;

import jakarta.persistence.*;

@Entity
public class UserProfile {

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    @Column(length = 1000)
    private String notes;
    @jakarta.persistence.Id
    private Long userId;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return userId;
    }
}