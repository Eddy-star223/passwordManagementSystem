package passwordApp.dtos.response;

import passwordApp.model.data.PasswordEntry;
import passwordApp.utils.EncryptionUtil;

public class passwordUpdateResponse {
    private Long entryId;
    private String siteUrl;
    private String username;
    private String decryptedPassword;

    public passwordUpdateResponse(PasswordEntry entry) {
        this.entryId = entry.getEntryId();
        this.siteUrl = entry.getSiteUrl();
        this.username = entry.getUsername();
        this.decryptedPassword = EncryptionUtil.decrypt(entry.getEncryptedPassword());
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public Long getEntryId() {
        return entryId;
    }

}