package com.osayijoy.common_utils_library.soteria.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "digicore.soteria")
public class SoteriaConfig {

    private String jwtSigningKey = "hjgiybkgiyggiyfutf675747crt7cc7rre5y";
    private String jwtKeyStorePath = "keys/iris.jks";
    private String jwtKeyStorePassword = "hjgiybkgiyggiyfutf675747crt7cc7rre5y";
    private String jwtKeyAlias = "securedJwt";
    private Long jwtExpiryInMinutes ;
    private boolean toRegisterDeviceOnLogin;
    private boolean toSwitchDeviceOnLogin;
    private boolean toAddDeviceOnLogin;

    private String allowedOrigins;

    private String[] allowedEndpointsWithoutAuthentication = {"/login","/reset","test"};

    //SYSTEM ROLE CONFIGS
    private String systemAdminRoleName = "SYSTEM_ADMIN";

    private String systemAdminEmail = "togidan@digicoreltd.com";

    private String systemGeneratedRolesBasePath = "config/roles";
    private String systemGeneratedRolesFileExtension = ".json";

    private List<String> systemGeneratedRolesPath = List.of(systemGeneratedRolesBasePath.concat("/").concat(systemAdminRoleName.concat(systemGeneratedRolesFileExtension)));
}
