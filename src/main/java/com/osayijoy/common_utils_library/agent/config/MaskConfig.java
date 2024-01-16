package com.osayijoy.common_utils_library.agent.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaskConfig {
    private String fieldName;
    private Character maskingElement;
    private Integer clearPrefix;
    private Integer clearSuffix;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Character getMaskingElement() {
        if (maskingElement == null)
            maskingElement = '*';
        return maskingElement;
    }

    public void setMaskingElement(Character maskingElement) {
        this.maskingElement = maskingElement;
    }

    public Integer getClearPrefix() {
        return clearPrefix;
    }

    public void setClearPrefix(Integer clearPrefix) {
        this.clearPrefix = clearPrefix;
    }

    public Integer getClearSuffix() {
        return clearSuffix;
    }

    public void setClearSuffix(Integer clearSuffix) {
        this.clearSuffix = clearSuffix;
    }
}
