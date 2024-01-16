package com.osayijoy.common_utils_library.helper.services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-12(Mon)-2022
 */
@Service
@Slf4j
public class ApiClientService {

    private final RestTemplate restTemplate;

    public ApiClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T,V> T postRequestWithQueryParamsAndBasicAuthorizationHeader(String url, V request, Class<T> clazz, MediaType mediaType, MultiValueMap<String,String> queryParams, String clientId, String secret)  {
        return getResponse(getUri(url, queryParams), request, clazz,getHttpHeadersUsingBasicAuthAsAuthorizationHeader(mediaType,clientId,secret),HttpMethod.POST).getBody();
    }

    public <T,V> T postRequestWithBearerAuthorizationHeader(String url, V request, Class<T> clazz, MediaType mediaType, String token)  {
        return getResponse(url, request, clazz, getHttpHeaders(mediaType,token),HttpMethod.POST).getBody();
    }

    public <T,V> T postRequestWithApiKeyAsAuthorizationHeader(String url, V request, Class<T> clazz, MediaType mediaType, String headerType, String apiKey)  {
        return getResponse(url, request, clazz, getHttpHeaders(mediaType, headerType, apiKey),HttpMethod.POST).getBody();
    }


    public <T,V> T postRequest(String url, V request, Class<T> clazz, MediaType mediaType)  {
      return getResponse(url, request, clazz, getHttpHeaders(mediaType,null),HttpMethod.POST).getBody();
    }

    public <V> HttpStatusCode postRequest(String url, V request)  {
        return getResponse(url, request, String.class,getHttpHeaders(MediaType.APPLICATION_JSON,null),HttpMethod.POST).getStatusCode();
    }

    private <T, V> ResponseEntity<T> getResponse(String url, V request, Class<T> clazz, HttpHeaders httpHeaders, HttpMethod httpMethod) {
        log.trace(">>>>>>>> making {} request to : {} with payload : {} >>>>>>>>>> " ,httpMethod.name(), url, request);

        ResponseEntity<T> entity = restTemplate.exchange(url, httpMethod, getHttpEntity(request, httpHeaders), clazz);

        log.trace(">>>>>>>> received response from : {} with response body  : {}  >>>>>>>>>> " , url, entity.getBody());
        return entity;
    }

    private <T, V> ResponseEntity<T> getResponse(String url, V request, V maskedRequestForLogger, Class<T> clazz, HttpHeaders httpHeaders, HttpMethod httpMethod) {
        log.trace(">>>>>>>> making {} request to : {} with payload : {} >>>>>>>>>> " ,httpMethod.name(), url, maskedRequestForLogger);

        ResponseEntity<T> entity = restTemplate.exchange(url, httpMethod, getHttpEntity(request, httpHeaders), clazz);

        log.trace(">>>>>>>> received response from : {} with response body  : {}  >>>>>>>>>> " , url, entity.getBody());
        return entity;
    }

    public <T,V> T getRequestWithQueryParams(String url, Class<T> clazz, MediaType mediaType, Map<String, String> requestParams)  {
        return getResponseWithRequestParams(url, clazz, getHttpHeaders(mediaType),HttpMethod.GET, requestParams).getBody();
    }

    public <T,V> T postRequestWithQueryParams(String url, Class<T> clazz, MediaType mediaType, Map<String, String> requestParams)  {
        return getResponseWithRequestParams(url, clazz, getHttpHeaders(mediaType),HttpMethod.POST, requestParams).getBody();
    }

    private <T, V> ResponseEntity<T> getResponseWithRequestParams(String url, Class<T> clazz, HttpHeaders httpHeaders,
                                                                  HttpMethod httpMethod, Map<String, String> requestParams) {
        log.trace(">>>>>>>> making {} request to : {} with payload : {} >>>>>>>>>> " ,httpMethod.name(), url);

        MultiValueMap<String, String> contents = new LinkedMultiValueMap<>();

        requestParams.forEach(contents::add);

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
                                    .queryParams(contents)
                                    .build();

        ResponseEntity<T> entity = restTemplate.exchange(builder.toUriString(), httpMethod, getHttpEntity(null, httpHeaders), clazz);

        log.trace(">>>>>>>> received response from : {} with response body  : {}  >>>>>>>>>> " , url, entity.getBody());
        return entity;
    }

    public <T,U> ResponseEntity<T> postRequestWithHeaders(String url, U request, HttpHeaders httpHeaders, Class<T> clazz){
        return getResponse(url, request, clazz, httpHeaders, HttpMethod.POST);
    }
    public <T,U> ResponseEntity<T> getRequestWithHeaders(String url, U request, HttpHeaders httpHeaders, Class<T> clazz){
        return getResponse(url, request, clazz, httpHeaders, HttpMethod.GET);
    }
    private  <V> HttpEntity<?> getHttpEntity(V request, HttpHeaders httpHeaders) {
        return new HttpEntity<>(request, httpHeaders);
    }

    private  HttpHeaders getHttpHeadersUsingBasicAuthAsAuthorizationHeader(MediaType mediaType, String clientId, String clientSecret) {
        HttpHeaders httpHeaders = getHttpHeaders(mediaType);
        httpHeaders.add("Authorization", generateBasicAuth(clientId, clientSecret));
        return httpHeaders;
    }

    private  HttpHeaders getHttpHeaders(MediaType mediaType, String token) {
        HttpHeaders httpHeaders = getHttpHeaders(mediaType);
        if (token != null && !token.isEmpty())
            httpHeaders.add("Authorization","Bearer " + token);

        return httpHeaders;
    }

    private  HttpHeaders getHttpHeaders(MediaType mediaType, String headerType, String apiKey) {
        HttpHeaders httpHeaders = getHttpHeaders(mediaType);
        if (apiKey != null && !apiKey.isEmpty())
            httpHeaders.add(headerType, apiKey);

        return httpHeaders;
    }

    private static HttpHeaders getHttpHeaders(MediaType mediaType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        //    httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
            httpHeaders.setAccept(List.of(mediaType));
        httpHeaders.setContentType(mediaType);
        return httpHeaders;
    }

    public <T,V> T getRequestWithQueryParamsAndBearerAuthorizationHeader(String url, V request, Class<T> clazz, MediaType mediaType, MultiValueMap<String,String> queryParams, String token)  {
        return getResponse(getUri(url, queryParams), request, clazz, getHttpHeaders(mediaType,token),HttpMethod.GET).getBody();

    }

    public <T,V> T postRequestWithQueryParams(String url, V request, Class<T> clazz, MediaType mediaType, MultiValueMap<String,String> queryParams)  {
        return getResponse(getUri(url, queryParams), request, clazz, getHttpHeaders(mediaType, null),HttpMethod.POST).getBody();

    }

    public <T,V> T getRequestWithQueryParams(String url, V request, Class<T> clazz, MediaType mediaType, MultiValueMap<String,String> queryParams)  {
        return getResponse(getUri(url, queryParams), request, clazz, getHttpHeaders(mediaType, null),HttpMethod.GET).getBody();

    }

    private static String getUri(String url, MultiValueMap<String, String> queryParams) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParams(queryParams)
                .build()
                .toUri().toString();
    }

    public <T,V> T getRequest(String url, V request, Class<T> clazz,MediaType mediaType)  {
        return getResponse(url, request, clazz, getHttpHeaders(mediaType,null),HttpMethod.GET).getBody();
    }

    public<T, V> ResponseEntity<T>  getRequestWithHeadersReturned(String url, V request, Class<T> clazz,MediaType mediaType)  {
        return getResponse(url, request, clazz, getHttpHeaders(mediaType,null),HttpMethod.GET);
    }


    public static String generateBasicAuth(String clientId, String clientSecret) {
        String clientIdSecretString = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(clientIdSecretString.getBytes(StandardCharsets.UTF_8));
    }

    public <T,V> T genericRequestWithBasicAuthorizationHeader(String url, V requestBody, MultiValueMap<String,String> queryParams, Class<T> responseClass, MediaType mediaType, HttpMethod httpMethod, String clientId, String clientSecret){
        return getResponse(getUri(url, queryParams), requestBody, responseClass, getHttpHeadersUsingBasicAuthAsAuthorizationHeader(mediaType, clientId, clientSecret), httpMethod).getBody();
    }

    public <T,V> T putRequestWithBasicAuthorizationHeader(String url, V requestBody, MultiValueMap<String,String> queryParams, Class<T> responseClass, String clientId, String clientSecret){
        return getResponse(getUri(url, queryParams), requestBody, responseClass, getHttpHeadersUsingBasicAuthAsAuthorizationHeader(MediaType.APPLICATION_JSON, clientId, clientSecret), HttpMethod.PUT).getBody();
    }

    public <T,V> T putRequestWithBasicAuthorizationHeaderAndMaskedSensitiveFieldsData(String url, V requestBody, V maskedRequestBodyForLogger, MultiValueMap<String,String> queryParams, Class<T> responseClass, String clientId, String clientSecret){
        return getResponse(getUri(url, queryParams), requestBody, maskedRequestBodyForLogger, responseClass, getHttpHeadersUsingBasicAuthAsAuthorizationHeader(MediaType.APPLICATION_JSON, clientId, clientSecret), HttpMethod.PUT).getBody();
    }
}
