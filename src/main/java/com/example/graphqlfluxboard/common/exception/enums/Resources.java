package com.example.graphqlfluxboard.common.exception.enums;

import lombok.Getter;

@Getter
public enum Resources {
    POST("게시글"),
    COMMENT("댓글"),
    REPLY("답글"),
    USER("유저"),
    USERNAME("유저이름");

    final String value;
    Resources(String value) {
        this.value = value;
    }
}
