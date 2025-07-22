package com.example.AI_QA.enums;

public enum QuestionStatus {
    WAITING("waiting"),
    ANSWERED("answered"),
    ERROR("error");

    private final String value;

    QuestionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
