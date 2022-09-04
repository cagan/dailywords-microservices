package com.dailywords.resource.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    private String definition;
    private String partOfSpeech;
    private String[] synonyms;
    private String[] typeOf;
    private String[] memberOf;
}

