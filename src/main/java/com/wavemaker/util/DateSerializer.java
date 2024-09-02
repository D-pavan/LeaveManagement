package com.wavemaker.util;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.sql.Date;

public class DateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.toString());
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Date.valueOf(json.getAsString());
    }
}
