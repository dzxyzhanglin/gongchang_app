package com.changdu.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by le on 2016/11/4.
 */
public class JsonToMap {

    public static JsonObject parseJson(String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        return jsonObj;
    }

    public static Map<String, Object> toMap(String json) {
        return JsonToMap.toMap(JsonToMap.parseJson(json));
    }

    public static Map<String, Object> toMap(JsonObject json) {
        Map<String, Object> map = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Map.Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ) {
            Map.Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JsonArray) {
                map.put((String) key, toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                map.put((String) key, toMap((JsonObject) value));
            } else {
                map.put((String) key, value);
            }
        }
        return map;
    }

    public static List<Object> toList(JsonArray json) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < json.size(); i++) {
            Object value = json.get(i);
            if (value instanceof JsonArray) {
                list.add(toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                list.add(toMap((JsonObject) value));
            } else {
                list.add(value);
            }
        }
        return list;
    }

    /**
     * json转List Map
     * @param json
     * @return
     */
    public static List<Map<String, Object>> toListMap(String json) {
        Gson gson = new Gson();
        List<Map<String, Object>> list = gson.fromJson(json, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        return list;
    }
}
