package com.osayijoy.common_utils_library.commonUtils.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Dec-06(Tue)-2022
 */
public class MD5Util {
    public static String digest(byte[] input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        return bytesToHex(md.digest(input));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
