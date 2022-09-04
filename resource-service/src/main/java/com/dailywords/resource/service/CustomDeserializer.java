package com.dailywords.resource.service;

import org.apache.kafka.common.serialization.Deserializer;

public class CustomDeserializer implements Deserializer<String> {
    @Override
    public String deserialize(String s, byte[] bytes) {
        System.out.println("deserialize");
        return null;
    }
}
