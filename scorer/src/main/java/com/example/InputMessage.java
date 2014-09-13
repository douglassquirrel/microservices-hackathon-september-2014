package com.example;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;

public class InputMessage {

    public String id;

    public List<Map> words;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
