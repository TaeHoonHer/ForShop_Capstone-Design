package com.forshop.project.dto.request;


import com.forshop.project.dto.ArticleCommentDto;
import com.forshop.project.dto.UserAccountDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ArticleCommentRequest(
        @NotNull(message = "articleId는 필수 값 입니다.")
        Long articleId,
        @NotBlank(message = "댓글 내용을 채워주세요")
        @Size(min = 5, max = 50, message = "댓글")
        String content) {

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
