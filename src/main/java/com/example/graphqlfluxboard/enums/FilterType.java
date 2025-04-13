package com.example.graphqlfluxboard.enums;

import lombok.Getter;

@Getter
public enum FilterType {
    NONE("NONE"),
    AUTHOR("AUTHOR"),
    TITLE("TITLE"),
    CONTENT("CONTENT"),
    TITLE_AND_CONTENT("TITLE_AND_CONTENT");

    private final String type;
    FilterType(String type) {
        this.type = type;
    }
}
