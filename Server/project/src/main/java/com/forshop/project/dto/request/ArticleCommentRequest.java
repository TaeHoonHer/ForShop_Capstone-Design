package com.forshop.project.dto.request;

import com.forshop.project.dto.ArticleCommentDto;
import com.forshop.project.dto.UserAccountDto;

public record ArticleCommentRequest(Long articleId, String content) {

    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
            articleId,
            userAccountDto,
            content
        );
    }
}