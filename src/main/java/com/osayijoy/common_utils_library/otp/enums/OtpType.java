package com.osayijoy.common_utils_library.otp.enums;

public enum OtpType {
    DEVICE_REGISTRATION("device registration"),
    PASSWORD_UPDATE("password update"),
    PASSWORD_UPDATE_RECOVERY_KEY("password update recovery key"),
    EMAIL_VERIFICATION("email verification"),
    TWO_FA_VERIFICATION("2FA verification"),
    PHONE_NUMBER_VERIFICATION("phone number verification"),
    BVN_VERIFICATION("phone number verification"),
    PRE_SIGN_UP("pre signup"),
    INVITATION("invitation"),

    CUSTOMER_REGISTRATION("customer registration"),

    CUSTOMER_REGISTRATION_MOBILE("customer registration mobile"),
    DEVICE_SWITCH("device switching");
    private String directive;

    OtpType(String directive) {
        this.directive = directive;
    }

    public String getDirective() {
        return directive;
    }
}
