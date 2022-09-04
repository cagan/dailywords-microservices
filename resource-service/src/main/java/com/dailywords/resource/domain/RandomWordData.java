package com.dailywords.resource.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RandomWordData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String word;

    private List<Result> results;

    @Override
    public String toString() {
        return "RandomWordData{" +
                "id='" + id + '\'' +
                ", word='" + word + '\'' +
                ", results=" + results +
                '}';
    }
}
