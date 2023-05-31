package com.forshop.project.controller;

import com.forshop.project.dto.UserAccountDto;
import com.forshop.project.dto.request.ArticleCommentRequest;
import com.forshop.project.dto.security.ServicePrincipal;
import com.forshop.project.service.ArticleCommentService;
import com.forshop.project.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;
    private final UserAccountService userAccountService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public String createArticleComment(
            @Validated @RequestBody ArticleCommentRequest articleCommentRequest,
            BindingResult bindingResult,
            @AuthenticationPrincipal ServicePrincipal servicePrincipal
            ) {

        Optional<UserAccountDto> userAccountDto = userAccountService.searchUser(servicePrincipal.getName());
        if (bindingResult.hasErrors()) {
            // 검증 오류가 있는 경우 처리
            StringBuilder errorMessage = new StringBuilder();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessage.append(fieldError.getField())
                        .append(": ")
                        .append(fieldError.getDefaultMessage())
                        .append("; ");
            }

            return errorMessage.toString();
        }
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(userAccountDto.get()));
        return "댓글 정보가 저장되었습니다.";
    }

    @PutMapping("/{commentId}")
    public String updateArticleComment(
            @Validated @RequestBody ArticleCommentRequest articleCommentRequest,
            BindingResult bindingResult,
            @AuthenticationPrincipal ServicePrincipal servicePrincipal
    ) {
        if (bindingResult.hasErrors()) {
            // 검증 오류가 있는 경우 처리
            StringBuilder errorMessage = new StringBuilder();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessage.append(fieldError.getField())
                        .append(": ")
                        .append(fieldError.getDefaultMessage())
                        .append("; ");
            }

            return errorMessage.toString();
        }

        articleCommentService.updateArticleComment(articleCommentRequest.toDto(servicePrincipal.toDto()));
        return "댓글 정보가 수정되었습니다.";
    }

    @DeleteMapping("/{commentId}")
    public String deleteArticleComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal ServicePrincipal servicePrincipal
    ) {

        articleCommentService.deleteArticleComment(commentId, servicePrincipal.getUsername());

        return "댓글 정보가 삭제되었습니다.";
    }


}
