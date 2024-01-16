package com.osayijoy.common_utils_library.helper.response;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ApiResponseJson<T> {
    private String message;
    private boolean success;
    private T data;
    private List<ApiError> errors = new ArrayList<>();

    public void addError(ApiError error){
        errors.add(error);
    }

    public void addError(String errorMessage){
        errors.add(new ApiError(errorMessage));
    }
}
