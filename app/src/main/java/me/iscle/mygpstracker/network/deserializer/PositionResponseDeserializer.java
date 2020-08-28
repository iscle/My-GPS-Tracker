package me.iscle.mygpstracker.network.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.iscle.mygpstracker.network.response.PositionResponse;

public class PositionResponseDeserializer implements JsonDeserializer<PositionResponse> {
    @Override
    public PositionResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            return ((PositionResponse[]) new Gson().fromJson(json, PositionResponse[].class))[0];
        } else {
            return new Gson().fromJson(json, PositionResponse.class);
        }
    }
}
