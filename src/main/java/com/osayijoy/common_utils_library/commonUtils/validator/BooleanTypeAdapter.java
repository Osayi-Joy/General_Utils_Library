package com.osayijoy.common_utils_library.commonUtils.validator;
/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-18(Tue)-2023
 */

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class BooleanTypeAdapter implements JsonDeserializer<Boolean> {
 @Override
 public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
  if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isBoolean()) {
   return json.getAsBoolean();
  } else {
   // Handle the case when the value is not a boolean in the JSON data
   return false; // or throw an exception, or set a custom default value
  }
 }
}
