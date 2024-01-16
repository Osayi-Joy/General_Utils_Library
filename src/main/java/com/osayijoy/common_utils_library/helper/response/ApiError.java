package com.osayijoy.common_utils_library.helper.response;

import lombok.*;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-09(Tue)-2022
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiError {
    public ApiError(String message) {
        this.message = message;
    }

    public static final String ERROR_UNKNOWN = "90";

    private String message;
    private String code;
}
