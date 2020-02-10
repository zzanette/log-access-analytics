package com.nabucco.petshop.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeGsonAdapter implements JsonSerializer<LocalDateTime> {

  @Override
  public JsonElement serialize(LocalDateTime dateTime, Type type,
      JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }
}
