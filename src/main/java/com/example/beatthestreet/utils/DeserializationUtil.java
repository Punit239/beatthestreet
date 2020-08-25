package com.example.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class DeserializationUtil {

	public static Object deserializeJsonString(String jsonString, Class clazz) {
        Object deserializedObject = null;
        try {
            deserializedObject = new ObjectMapper().readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return deserializedObject;
    }

    public static <T> List<T> deserializeJsonArrayString(String jsonArrayString) {
        List<T> deserializedArrayObject = null;
        try {
            deserializedArrayObject = (List<T>) new ObjectMapper().readValue(
                    jsonArrayString, new TypeReference<List<T>>() { });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return deserializedArrayObject;
    }
}