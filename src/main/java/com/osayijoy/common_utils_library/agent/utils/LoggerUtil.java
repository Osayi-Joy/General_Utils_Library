package com.osayijoy.common_utils_library.agent.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.osayijoy.common_utils_library.agent.config.MaskConfig;
import com.osayijoy.common_utils_library.agent.config.RedactionConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.w3c.dom.*;

@Slf4j
public class LoggerUtil {


    private LoggerUtil() {
    }

    public static String getRequestHeaders(HttpServletRequest request, List<MaskConfig> maskConfigs, List<RedactionConfig> redactionConfigs) {
        JSONObject headerData = new JSONObject();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headerData.put(headerName, request.getHeader(headerName));
            }
        }

        maskApplicableFields(headerData, maskConfigs);
        redactApplicableFields(headerData, redactionConfigs);
        return headerData.toString();
    }

    public static String getResponseHeaders(HttpServletResponse response, List<MaskConfig> maskConfigs, List<RedactionConfig> redactionConfigs) {
        JSONObject headerData = new JSONObject();
        Collection<String> headerNames = response.getHeaderNames();
        headerNames.forEach(name -> headerData.put(name, response.getHeader(name)));

        maskApplicableFields(headerData, maskConfigs);
        redactApplicableFields(headerData, redactionConfigs);
        return headerData.toString();
    }

    public static String getRequestBody(HttpServletRequest request, List<MaskConfig> maskConfigs, List<RedactionConfig> redactionConfigs) {
        ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
        if (wrapper != null) {
            if (HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
                return getUrlEncodedPayload(request, maskConfigs, redactionConfigs);
            } else {
                String contentType = request.getContentType();
                if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                    return getUrlEncodedPayload(request, maskConfigs, redactionConfigs);
                }
//                else if (contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
//                    return getMultipartFormPayload(request, maskConfigs, redactionConfigs);
//                }
                else if (contentType.contains(MediaType.APPLICATION_XML_VALUE)
                        || contentType.contains(MediaType.TEXT_XML_VALUE)) {
                    return getXmlPayload(wrapper, maskConfigs, redactionConfigs);
                } else if (contentType.contains(MediaType.TEXT_PLAIN_VALUE)) {
                    return getRequestBodyAsString(wrapper);
                } else {
                    // Use application Json as default
                    return getJSONPayload(wrapper, maskConfigs, redactionConfigs);
                }
            }
        }
        return "";
    }

    public static String getResponseBody(HttpServletResponse response, List<MaskConfig> maskingConfigs, List<RedactionConfig> redactionConfigs) {
        Object responseData = null;
        ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
        if (wrapper != null) {
            try {
                String contentType = response.getContentType();
                StringWriter writer = new StringWriter();
                IOUtils.copy(wrapper.getContentInputStream(), writer);
                String content = writer.toString();

                if (StringUtils.isNotBlank(content)) {
                    if (contentType.contains(MediaType.APPLICATION_XML_VALUE)
                            || contentType.contains(MediaType.TEXT_XML_VALUE)) {
                        Document xmlDoc = stringToXMLDoc(content);
                        if (xmlDoc != null) {
                            maskApplicableFields(xmlDoc, maskingConfigs);
                            redactApplicableFields(xmlDoc, redactionConfigs);
                            responseData = xmlDocToString(xmlDoc);
                        }
                    } else if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                        JSONObject json = new JSONObject(content);
                        maskApplicableFields(json, maskingConfigs);
                        redactApplicableFields(json, redactionConfigs);
                        responseData = json;
                    } else if (contentType.contains(MediaType.TEXT_PLAIN_VALUE)) {
                        responseData = content;
                    }
                }
            } catch (Exception ex) {
                log.error("Unable to parse response body... {}", ex);
            }
        }

        try {
            if (wrapper != null)
                wrapper.copyBodyToResponse();
        } catch (Exception ex) {
            log.error("Fatal exception. Unable to retrieve response body from cache - {}", ex);
        }

        return responseData.toString();
    }

    public static Timestamp currentTimestamp() {
        return Timestamp.from(
                ZonedDateTime.now(ZoneId.of("Africa/Lagos")).toInstant()
        );
    }

    private static String getUrlEncodedPayload(HttpServletRequest request, List<MaskConfig> maskConfigs, List<RedactionConfig> redactionConfigs) {
        JSONObject payload = new JSONObject();
        Enumeration<String> fieldNames = request.getParameterNames();
        if (fieldNames != null) {
            while (fieldNames.hasMoreElements()) {
                String fieldName = fieldNames.nextElement();
                payload.put(fieldName, request.getParameter(fieldName));
            }
        }
        maskApplicableFields(payload, maskConfigs);
        redactApplicableFields(payload, redactionConfigs);
        return payload.toString();
    }

//    private static String getMultipartFormPayload(HttpServletRequest request, List<MaskConfig> maskConfigs, List<RedactionConfig> redactionConfigs) {
//        JSONObject payload = new JSONObject();
//
//        try {
//            CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
//            MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart(request);
//            Enumeration<String> fieldNames = multipartRequest.getParameterNames();
//            if (fieldNames != null) {
//                while (fieldNames.hasMoreElements()) {
//                    String fieldName = fieldNames.nextElement();
//                    payload.put(fieldName, request.getParameter(fieldName));
//                }
//            }
//        } catch (Exception ex) {
//            log.error("Unable to parse multipart form data - {}", ex.getMessage());
//        }
//
//        maskApplicableFields(payload, maskConfigs);
//        redactApplicableFields(payload, redactionConfigs);
//        return payload.toString();
//    }

    private static String getXmlPayload(ContentCachingRequestWrapper wrapper, List<MaskConfig> maskingConfigs, List<RedactionConfig> redactionConfigs) {
        String body = getRequestBodyAsString(wrapper);
        Document xmlDoc = stringToXMLDoc(body);
        if (xmlDoc != null) {
            maskApplicableFields(xmlDoc, maskingConfigs);
            redactApplicableFields(xmlDoc, redactionConfigs);
            return xmlDocToString(xmlDoc);
        }

        return "";
    }

    private static String getJSONPayload(ContentCachingRequestWrapper wrapper, List<MaskConfig> maskingConfigs, List<RedactionConfig> redactionConfigs) {
        JSONObject payload = new JSONObject();
        String body = getRequestBodyAsString(wrapper);
        if (StringUtils.isNotBlank(body)) {
            payload = new JSONObject(body);
        }

        maskApplicableFields(payload, maskingConfigs);
        redactApplicableFields(payload, redactionConfigs);
        return payload.toString();
    }

    private static String getRequestBodyAsString(ContentCachingRequestWrapper wrapper) {
        byte[] buf = wrapper.getContentAsByteArray();
        if (buf != null && buf.length > 0) {
            try {
                return new String(buf, wrapper.getCharacterEncoding());
            } catch (UnsupportedEncodingException ex) {
                log.error("UnsupportedEncodingException", ex);
            }
        }

        return "";
    }

    private static void maskApplicableFields(JSONObject jsonObject, List<MaskConfig> maskingConfig) {
        Iterator<String> fieldNames = jsonObject.keys();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();

            if ((jsonObject.optJSONArray(fieldName) == null) && (jsonObject.optJSONObject(fieldName) == null)) {
                // Is not an array nor an object so we treat as direct value
                Optional<MaskConfig> optionalMaskingConfig = maskingConfig.stream()
                        .filter(config -> config.getFieldName().equalsIgnoreCase(fieldName))
                        .findFirst();
                if (optionalMaskingConfig.isPresent()) {
                    MaskConfig config = optionalMaskingConfig.get();
                    Object targetObject = jsonObject.get(fieldName);
                    if (!(targetObject instanceof String)) {
                        targetObject = objectToString(targetObject);
                    }

                    String targetValue = (String) targetObject;
                    targetValue = maskString(targetValue, config.getMaskingElement(), config.getClearPrefix(), config.getClearSuffix());

                    jsonObject.put(fieldName, targetValue);
                }
            } else if (jsonObject.optJSONObject(fieldName) != null) {
                // JSON object... Recursive masking
                maskApplicableFields(jsonObject.getJSONObject(fieldName), maskingConfig);
            } else if (jsonObject.optJSONArray(fieldName) != null) {
                // JSON array... recursive masking on each item
                JSONArray jArray = jsonObject.getJSONArray(fieldName);
                for (int i = 0; i < jArray.length(); i++) {
                    if ((jArray.optJSONArray(i) == null) && (jArray.optJSONObject(i) == null))
                        // Array of primitive types
                        continue;

                    maskApplicableFields(jArray.getJSONObject(i), maskingConfig);
                }
            }
        }
    }

    private static void maskApplicableFields(Document document, List<MaskConfig> maskingConfig) {
        NodeList nodeList = document.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tag = nodeList.item(i);
            maskXMLTag(tag, maskingConfig);
        }
    }

    private static void redactApplicableFields(JSONObject jsonObject, List<RedactionConfig> redactionConfig) {
        Iterator<String> fieldNames = jsonObject.keys();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();

            if ((jsonObject.optJSONArray(fieldName) == null) && (jsonObject.optJSONObject(fieldName) == null)) {
                // Is not an array nor an object so we treat as direct value
                Optional<RedactionConfig> optionalRedactionConfig = redactionConfig.stream()
                        .filter(config -> config.getFieldName().equalsIgnoreCase(fieldName))
                        .findFirst();
                optionalRedactionConfig.ifPresent(config -> jsonObject.put(fieldName, config.getRedactedValue()));
            } else if (jsonObject.optJSONObject(fieldName) != null) {
                // JSON object... Recursive masking
                redactApplicableFields(jsonObject.getJSONObject(fieldName), redactionConfig);
            } else if (jsonObject.optJSONArray(fieldName) != null) {
                // JSON array... recursive masking on each item
                JSONArray jArray = jsonObject.getJSONArray(fieldName);
                for (int i = 0; i < jArray.length(); i++) {
                    if ((jArray.optJSONArray(i) == null) && (jArray.optJSONObject(i) == null))
                        // Array of primitive types
                        continue;

                    redactApplicableFields(jArray.getJSONObject(i), redactionConfig);
                }
            }
        }
    }

    private static void redactApplicableFields(Document document, List<RedactionConfig> redactionConfigs) {
        NodeList nodeList = document.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tag = nodeList.item(i);
            redactXMLTag(tag, redactionConfigs);
        }
    }

    private static void maskXMLTag(Node node, List<MaskConfig> maskingConfig) {
        // Mask applicable attributes
        maskXMLTagAttributes(node, maskingConfig);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                maskXMLTag(currentNode, maskingConfig);
            } else if (currentNode.getNodeType() == Node.TEXT_NODE) {
                // Check field value
                String fieldName = currentNode.getParentNode().getNodeName();
                Optional<MaskConfig> optionalFieldMask = maskingConfig.stream()
                        .filter(config -> config.getFieldName().equalsIgnoreCase(fieldName))
                        .findFirst();
                if (optionalFieldMask.isPresent()) {
                    MaskConfig config = optionalFieldMask.get();
                    currentNode.setTextContent(maskString(currentNode.getTextContent(), config.getMaskingElement(), config.getClearPrefix(), config.getClearSuffix()));
                }
            }
        }
    }

    private static void maskXMLTagAttributes(Node node, List<MaskConfig> maskingConfig) {
        NamedNodeMap attributeMap = node.getAttributes();
        if (attributeMap != null && attributeMap.getLength() > 0) {
            for (int a = 0; a < attributeMap.getLength(); a++) {
                Node attribute = attributeMap.item(a);
                String attributeName = attribute.getNodeName();
                Optional<MaskConfig> optionalAttributeMask = maskingConfig.stream()
                        .filter(config -> config.getFieldName().equalsIgnoreCase(attributeName))
                        .findFirst();

                if (optionalAttributeMask.isPresent()) {
                    MaskConfig config = optionalAttributeMask.get();
                    ((Element) node).setAttribute(attributeName, maskString(attribute.getNodeValue(), config.getMaskingElement(), config.getClearPrefix(), config.getClearSuffix()));
                }
            }
        }
    }

    private static void redactXMLTag(Node node, List<RedactionConfig> redactionConfig) {
        // Redact applicable attributes
        redactXMLTagAttributes(node, redactionConfig);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                redactXMLTag(currentNode, redactionConfig);
            } else if (currentNode.getNodeType() == Node.TEXT_NODE) {
                String fieldName = currentNode.getParentNode().getNodeName();
                Optional<RedactionConfig> optionalRedactionConfig = redactionConfig.stream()
                        .filter(config -> config.getFieldName().equalsIgnoreCase(fieldName))
                        .findFirst();
                if (optionalRedactionConfig.isPresent()) {
                    RedactionConfig config = optionalRedactionConfig.get();
                    currentNode.setTextContent(config.getRedactedValue());
                }
            }
        }
    }

    private static void redactXMLTagAttributes(Node node, List<RedactionConfig> redactionConfig) {
        NamedNodeMap attributeMap = node.getAttributes();
        if (attributeMap != null && attributeMap.getLength() > 0) {
            for (int a = 0; a < attributeMap.getLength(); a++) {
                Node attribute = attributeMap.item(a);
                String attributeName = attribute.getNodeName();
                Optional<RedactionConfig> optionalAttributeMask = redactionConfig.stream()
                        .filter(config -> config.getFieldName().equalsIgnoreCase(attributeName))
                        .findFirst();

                if (optionalAttributeMask.isPresent()) {
                    RedactionConfig config = optionalAttributeMask.get();
                    ((Element) node).setAttribute(attributeName, config.getRedactedValue());
                }
            }
        }
    }

    private static String maskString(String rawText, char maskChar, int clearPrefix, int clearSuffix) {
        int dataLength = rawText.length();
        int overlayLength = dataLength - clearPrefix - clearSuffix;

        String mask = StringUtils.repeat(String.valueOf(maskChar), overlayLength);
        return StringUtils.overlay(rawText, mask, clearPrefix, dataLength - clearSuffix);
    }

    private static String objectToString(Object object) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            return ow.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            log.error("Unable to convert object to string - {}", ex.getMessage());
        }

        return "";
    }

    private static Document stringToXMLDoc(String data) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(new ByteArrayInputStream(data.getBytes()));
        } catch (Exception ex) {
            log.error("Unable to parse XML data - {}", ex.getMessage());
            return null;
        }
    }

    private static String xmlDocToString(Document document) {
        try {
            DOMSource domSource = new DOMSource(document);
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);

            return sw.toString();
        } catch (Exception ex) {
            log.error("Unable to convert xml document to String - {}", ex.getMessage());
            return "";
        }
    }
}
