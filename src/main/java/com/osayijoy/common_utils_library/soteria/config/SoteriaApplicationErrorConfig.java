package com.osayijoy.common_utils_library.soteria.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "digicore.soteria.application")
public class SoteriaApplicationErrorConfig {

    private String errorAuthCodePrefix = "ERR_AUT_";
    private String errorGenCodePrefix = "ERR_GEN_";
    private String adminHelpSuggestionMessage = "Please contact administrator for help";
    private String loginFailedErrorCode = errorAuthCodePrefix.concat("420");
    private String loginFailedErrorMessage = "You are not Authorized";
    private String badCredentialErrorCode = errorAuthCodePrefix.concat("421");
    private String badCredentialErrorMessage = "invalid username and password combination";
    private String accountLockedErrorCode = errorAuthCodePrefix.concat("422");
    private String accountLockedErrorMessage = "Account is Locked";
    private String userNotFoundErrorCode = errorAuthCodePrefix.concat("423");
    private String userNotFoundErrorMessage = "User Does Not Exist";
    private String accountDisabledErrorCode = errorAuthCodePrefix.concat("424");
    private String accountDisabledErrorMessage = String.format("Your account has been disabled, %s",adminHelpSuggestionMessage);
    private String accountDeactivatedErrorCode = errorAuthCodePrefix.concat("425");
    private String accountDeactivatedErrorMessage = String.format("Your account is in-active %s", adminHelpSuggestionMessage);
    private String userExistErrorCode = errorGenCodePrefix.concat("426");
    private String userExistErrorMessage = "Username already exists";

    private String badRequestErrorCode = errorGenCodePrefix.concat("427");
    private String badRequestErrorMessage = "Request is invalid";

    private String emailExistErrorCode = errorGenCodePrefix.concat("428");

    private String emailExistErrorMessage = "Email already exists";

    private String permissionRequiredErrorCode = errorGenCodePrefix.concat("429");
    private String permissionRequiredMessage = "No Permission Supplied";

    private String roleNotFoundErrorCode = errorGenCodePrefix.concat("430");
    private String roleNotFoundMessage = "Invalid Role Supplied";
    private String invalidAgentStatusErrorMessage = "Agent status is invalid";

    private String invalidAgentTypeErrorMessage = "Agent Type is invalid";

    private String agentSourceOfRecruitmentTypeErrorMessage = "Recruitment source does not exist";

    private String invalidBranchTypeErrorMessage = "Branch does not exist";

    private String invalidAgentSystemConfigurationErrorCode = errorGenCodePrefix.concat("431");

    private String invalidAgentSystemConfigurationErrorMessage = "Invalid System configuration";


    public String passwordMisMatchErrorMessage = "Password Mismatch, check again";
    public String passwordMisMatchErrorCode = errorGenCodePrefix.concat("431");
}
