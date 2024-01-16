package com.osayijoy.common_utils_library.agent.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;



@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoggerConfig {
    private List<String> uriBlacklist;

    @JsonProperty("maskedFields")
    private List<MaskConfig> maskConfigs;

    @JsonProperty("redactedFields")
    private List<RedactionConfig> redactionConfigs;

    public List<String> getUriBlacklist() {
        return uriBlacklist == null ? new ArrayList<>() : uriBlacklist;
    }

    public void setUriBlacklist(List<String> uriBlacklist) {
        this.uriBlacklist = uriBlacklist;
    }

    public List<MaskConfig> getMaskConfigs() {
        return maskConfigs == null ? new ArrayList<>() : maskConfigs;
    }

    public void setMaskConfigs(List<MaskConfig> maskConfigs) {
        this.maskConfigs = maskConfigs;
    }

    public List<RedactionConfig> getRedactionConfigs() {
        return redactionConfigs == null ? new ArrayList<>() : redactionConfigs;
    }

    public void setRedactionConfigs(List<RedactionConfig> redactionConfigs) {
        this.redactionConfigs = redactionConfigs;
    }
}
