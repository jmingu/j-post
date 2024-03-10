package com.post.common.configuration.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CryptoUtil {


    // 암호화
    public static String encrypt(String encryptData, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
        byte[] encrypted = cipher.doFinal(encryptData.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // 복호화
    public static String decrypt(String header) throws Exception {

        final String key = header.substring(0, 16).trim(); // 암호화 랜덤키
        final String value = header.substring(16).trim(); // 암호화 된 정보

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(value));

        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
