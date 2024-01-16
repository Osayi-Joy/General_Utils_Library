package com.osayijoy.common_utils_library.commonUtils.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Oluwatobi Ogunwuyi
 * @since May-18(Thu)-2023
 */

@UtilityClass
public class CardUtil {

    public  String maskCardPan(String pan) {
        int endIndex = pan.length() - 4;
        int startIndex = 6;
        String mask = "*";
        return startIndex >= endIndex ? maskCardPan(pan, 0, endIndex, mask) : maskCardPan(pan, startIndex, endIndex, mask);
    }



    private  String maskCardPan(String pan, int start, int end, String mask) {
        int lengthOfMask = end - start;
        return StringUtils.overlay(pan, mask.repeat(lengthOfMask), start, end);
    }
}
