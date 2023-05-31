package com.forshop.project.dto.response;

import com.forshop.project.dto.ArticleDto;
import com.forshop.project.dto.HashtagDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleResponse( //게시글과 사진과 해시태그
        Long id,
        String title,
        String content,
        String storedName,
        String realName,
        Set<String> hashtags,
        LocalDateTime createdAt,
        String email,
        String nickname
) {

    public static ArticleResponse of(Long id, String title, String content, String storedName, String realName,Set<String> hashtags, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleResponse(id, title, content, storedName, realName, hashtags, createdAt, email, nickname);
    }

    public static ArticleResponse from(ArticleDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().email();
        }

        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.photoDto().storedName(),
                dto.photoDto().realName(),
                dto.hashtagDtos().stream()
                                .map(HashtagDto::hashtagName)
                                .collect(Collectors.toUnmodifiableSet()),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }

}
