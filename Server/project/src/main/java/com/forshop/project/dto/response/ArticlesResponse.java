package com.forshop.project.dto.response;

import com.forshop.project.dto.ArticleDto;

import java.time.LocalDateTime;
import java.util.Set;

public record ArticlesResponse (
        Long id,
        String storedName
){
    public static ArticlesResponse of(Long id, String storedName) {
        return new ArticlesResponse(id, storedName);
    }

    public static ArticlesResponse from(ArticleDto dto) {
        return new ArticlesResponse(dto.id(), dto.photoDto().storedName());
    }
}
