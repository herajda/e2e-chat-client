package main.java.com.e2e.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;


public class SerializationUtils {
    public static String serializeMessageToJSON(Message message) throws Exception {
        return serializeObjectToJson(message);
    }
    public static Message deserializeMessageFromJSON(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, Message.class);
    }


    private static String serializeObjectToJSON(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
