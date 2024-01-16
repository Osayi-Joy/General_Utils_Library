package com.osayijoy.common_utils_library.registhentication.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@UtilityClass
@Slf4j
public class AESUtil {

    @Value("${aes.encryption.key:1P3YA29TRV705IK5}")
    private String key;
    @Value("${aes.encryption.iv:1P3YA29TRV705IK5}")
    private String iv;

    public String decrypt(String encryptedData) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encrypted1 = decoder.decode(encryptedData);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);

            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
