package com.osayijoy.common_utils_library.commonUtils.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.osayijoy.common_utils_library.commonUtils.validator.BooleanTypeAdapter;
import com.osayijoy.common_utils_library.commonUtils.validator.LocalDateTimeTypeAdapter;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class ClientUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

   private final Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z]+)|([A-Z]))");


    public String getIpAddress(HttpServletRequest request) {
        String ip = Optional.ofNullable(request.getHeader("X-Real-IP")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return ip;
    }
    public String getIpAddressFromRequest(HttpServletRequest request) {
        String ip = Optional.ofNullable(request.getHeader("X-Real-IP")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "localhost";
        return ip;
    }

    public String getLoggedInUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return "SYSTEM";
        return auth.getName();
    }

    public ObjectMapper getObjectMapper(){
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public Gson getGsonMapper(String dateFormat){
       return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(dateFormat))
                .registerTypeAdapter(Boolean.class,new BooleanTypeAdapter())
                .create();
    }

    public Gson getGsonMapper(){
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .registerTypeAdapter(Boolean.class,new BooleanTypeAdapter())
                .create();
    }

    private List<String> findWordsInMixedCase(String text) {
        Matcher matcher = WORD_FINDER.matcher(text);
        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            words.add(matcher.group(0));
        }
        return words;
    }

    private String wordCase(String word) {
        return word.substring(0, 1).toUpperCase()
                + word.substring(1).toLowerCase();
    }

    public String capitalizeFirstLetter(String word){
        return word.substring(0, 1).toUpperCase()
                + word.substring(1);
    }

    public static String transformToSnakeCase(String input) {
        int inputLength = input.length();
        char[] resultChars = new char[inputLength * 2]; // Allocate a char array with room for underscores

        int resultIndex = 0;
        boolean underscoreNeeded = false;

        for (int i = 0; i < inputLength; i++) {
            char currentChar = input.charAt(i);

            if (Character.isUpperCase(currentChar)) {
                if (underscoreNeeded) {
                    resultChars[resultIndex++] = '_';
                }
                resultChars[resultIndex++] = Character.toLowerCase(currentChar);
                underscoreNeeded = true;
            } else {
                resultChars[resultIndex++] = currentChar;
            }
        }

        return new String(resultChars, 0, resultIndex).toUpperCase(); // Convert the transformed part to uppercase
    }

    public String sentenceCase(String text) {
       List<String> words =  findWordsInMixedCase(text);
        List<String> capitalized = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            String currentWord = words.get(i);
            if (i == 0) {
                capitalized.add(wordCase(currentWord));
            } else {
                capitalized.add(currentWord.toLowerCase());
            }
        }
        return String.join(" ", capitalized) + ".";
    }

    public String getFormattedDate() {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("h:mm:ss a, MMMM d, Y"));

    }

    public String commaSeparatedName(@NonNull String name){
        return name.concat(",");
    }

    public String getReceiptDate() {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MMM d, Y | h:mm a"));
    }

    public String getReceiptDate(LocalDateTime localDateTime) {
        return localDateTime.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MMM d, Y | h:mm a"));
    }


    public  String encodeAccountNumber(@Nullable String input) {
        if(input == null) return "00*****000";
        int length = input.length();
        if (length <= 6) {
            return input;
        } else {
            return input.substring(0, 2) + "*****" + input.substring(length - 3, length);
        }
    }

    public boolean validateEmail(String email){
        return EmailValidator.getInstance().isValid(email) || email.endsWith("care") || email.endsWith("care") || email.endsWith("africa");
    }


    public boolean validatePhoneNumber(String phoneNumber) {
        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }





}

