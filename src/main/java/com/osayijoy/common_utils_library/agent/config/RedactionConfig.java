package com.osayijoy.common_utils_library.agent.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedactionConfig {
    private String fieldName;
    private String redactedValue;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getRedactedValue() {
        return redactedValue;
    }

    public void setRedactedValue(String redactedValue) {
        this.redactedValue = redactedValue;
    }
}
