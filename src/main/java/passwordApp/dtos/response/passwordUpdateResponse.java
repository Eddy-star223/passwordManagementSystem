package passwordApp.dtos.response;

import passwordApp.model.data.PasswordEntry;
import passwordApp.utils.EncryptionUtil;

public class passwordUpdateResponse {
    private Long entryId;
    private String siteName;
    private String username;
    private String decryptedPassword;

    public passwordUpdateResponse(PasswordEntry entry) {
        this.entryId = entry.getEntryId();
        this.siteName = entry.getSiteName();
        this.username = entry.getUsername();
        this.decryptedPassword = EncryptionUtil.decrypt(entry.getEncryptedPassword());
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getSiteName() {
        return siteName;
    }

    public Long getEntryId() {
        return entryId;
    }

}