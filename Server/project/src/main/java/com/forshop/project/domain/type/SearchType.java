package com.forshop.project.domain.type;

import lombok.Getter;

public enum SearchType {
    TITLE("제목"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그");

    @Getter
    private final String description;

    SearchType(String description) {
        this.description = description;
    }
}