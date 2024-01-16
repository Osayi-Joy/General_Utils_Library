package com.osayijoy.common_utils_library.commonUtils.util;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-22(Thu)-2022
 */
@Component
@Slf4j
public class AESUtil {

    @Value("${sdk.aes.encryption.key:1P3YA29TRV705IK5}")
    private String sdkKey;
    @Value("${sdk.aes.encryption.iv:1P3YA29TRV705IK5}")
    private String sdkIv;

    @Value("${integration.aes.encryption.iv:7P0BE63QXD312GZ8}")
    private String apiIv;

    public String decrypt(String encryptedData) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encrypted1 = decoder.decode(encryptedData);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(sdkKey.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(sdkIv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);

            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String decrypt(String encryptedData, String key) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encrypted1 = decoder.decode(encryptedData);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(apiIv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);

            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] generateRandomIV() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16]; // IV length is fixed at 16 bytes for AES
        secureRandom.nextBytes(iv);
        return iv;
    }
}
