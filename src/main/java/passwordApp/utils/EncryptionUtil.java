package passwordApp.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "1234567890123456";
    private static final String IV = "1234567890123456";

    private static SecretKeySpec getSecretKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    private static IvParameterSpec getIv() {
        return new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
    }

    public static String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            throw new IllegalArgumentException("Plain text cannot be null or empty");
        }

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), getIv());
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed: " + e.getMessage(), e);
        }
    }

    public static String decrypt(String cipherText) {
        if (cipherText == null || cipherText.isEmpty()) {
            throw new IllegalArgumentException("Cipher text cannot be null or empty");
        }

        try {
            byte[] decoded = Base64.getDecoder().decode(cipherText);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), getIv());
            byte[] decryptedBytes = cipher.doFinal(decoded);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed: " + e.getMessage(), e);
        }
    }
}