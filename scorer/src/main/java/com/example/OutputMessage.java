package com.example;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;

public class OutputMessage {

    public String id;

    public Map word;

    public int score;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
