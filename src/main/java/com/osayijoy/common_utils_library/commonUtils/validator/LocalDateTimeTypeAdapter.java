package com.osayijoy.common_utils_library.commonUtils.validator;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn May-02(Tue)-2023
 */
public class LocalDateTimeTypeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    private final DateTimeFormatter formatter;

    public LocalDateTimeTypeAdapter(String dateFormat) {
        formatter = new DateTimeFormatterBuilder()
                .appendPattern(dateFormat) // Date and time without fractional seconds
                .optionalStart()
                .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true) // Up to 9 fractional digits
                .optionalEnd()
                .toFormatter();
    }

    public LocalDateTimeTypeAdapter() {
        formatter = DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss");
    }


    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType,
                                 JsonSerializationContext context) {

        return new JsonPrimitive(formatter.format(localDateTime));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString().replace("T", " "), formatter);
    }
}
