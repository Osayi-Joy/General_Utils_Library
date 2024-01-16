package com.osayijoy.common_utils_library.helper.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseHandler {

    @JsonProperty("body")
    private ResponseEntity<String> successMessage;
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("hasError")
    private boolean hasError;



}
