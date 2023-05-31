package com.forshop.project.dto.response;

import com.forshop.project.dto.ArticleDto;
import com.forshop.project.dto.ArticleWithPhotoWithCommentDtos;
import com.forshop.project.dto.HashtagDto;
import com.forshop.project.dto.PhotoDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.LinkedHashSet;

public record ArticleWithPhotoWithCommentsResponse( //게시글 사진 댓글
        Long id,
        String title,
        String content,
        String storedName,
        String realName,
        Set<String> hashtags,
        LocalDateTime createdAt,
        String email,
        String nickname,
        String userId,
        Set<ArticleCommentResponse> articleCommentResponse
) {

    public static ArticleWithPhotoWithCommentsResponse of(Long id, String title, String content, String storedName, String realName,Set<String> hashtags, LocalDateTime createdAt, String email, String nickname, String userId, Set<ArticleCommentResponse> articleCommentsResponses) {
        return new ArticleWithPhotoWithCommentsResponse(id, title, content,storedName, realName, hashtags, createdAt, email, nickname, userId, articleCommentsResponses);
    }

    public static ArticleWithPhotoWithCommentsResponse from(ArticleWithPhotoWithCommentDtos dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().email();
        }

        return new ArticleWithPhotoWithCommentsResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.photoDto().storedName(),
                dto.photoDto().realName(),
                dto.hashtagDtos().stream()
                        .map(HashtagDto::hashtagName)
                        .collect(Collectors.toUnmodifiableSet())
                ,
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().email(),
                dto.articleCommentDtos().stream()
                        .map(ArticleCommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }
}