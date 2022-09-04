package com.dailywords.resource.service;

import com.dailywords.resource.domain.RandomWordData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class RandomWordDeserializer implements Deserializer<RandomWordData> {
    @Override
    public RandomWordData deserialize(String s, byte[] bytes) {
        try {
            return new ObjectMapper().readValue(bytes, RandomWordData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
