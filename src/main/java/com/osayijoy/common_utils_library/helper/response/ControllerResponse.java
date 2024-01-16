package com.osayijoy.common_utils_library.helper.response;
/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-04(Tue)-2023
 */

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerResponse {
 private ControllerResponse() {
 }

 private static final String DEFAULT_SUCCESS_MESSAGE = "Request Successfully Treated";
 public static ResponseEntity<Object> buildSuccessResponse(Object responseData, String message ){
  return ResponseEntity.ok(ApiResponseJson.builder()
          .success(true)

          .data(responseData)
          .message(StringUtils.isBlank(message)?DEFAULT_SUCCESS_MESSAGE:message)
          .errors(null)
          .build());

 }

 public static   ResponseEntity<Object> buildSuccessResponse(Object responseData ){
  return ResponseEntity.ok(ApiResponseJson.builder()
          .success(true)
          .data(responseData)
          .message(DEFAULT_SUCCESS_MESSAGE)
          .errors(null)
          .build());

 }

 public static   ResponseEntity<Object> buildSuccessResponse(){
  return ResponseEntity.ok(ApiResponseJson.builder()
          .success(true)

          .data(null)
          .message(DEFAULT_SUCCESS_MESSAGE)
          .errors(null)
          .build());

 }



 public static ResponseEntity<Object> buildFailureResponse(List<ApiError> apiErrors, HttpStatus httpStatus ){
  String message = "";
  if (httpStatus.is4xxClientError()){
   message = "Kindly ensure you are passing the right information, check the errors field for what could be wrong with your request";
  }else if (httpStatus.is5xxServerError()){
   message = "Oops, sorry we were unable to process your request, reach out to support for help";
  }
  return new ResponseEntity<>(ApiResponseJson.builder()
          .success(false)
          .data(null)
          .message(message)
          .errors(apiErrors)
          .build(), httpStatus);

 }
}
