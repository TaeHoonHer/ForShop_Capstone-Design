package com.forshop.project.controller;

import com.forshop.project.domain.type.SearchType;
import com.forshop.project.dto.request.UploadRequest;
import com.forshop.project.dto.response.ArticleWithPhotoWithCommentsResponse;
import com.forshop.project.dto.response.ArticlesResponse;
import com.forshop.project.dto.security.ServicePrincipal;
import com.forshop.project.service.ArticleService;
import com.forshop.project.service.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final S3UploaderService s3UploaderService;

    //게시글 가져오기
    @GetMapping
    public Page<ArticlesResponse> articles (
            @RequestParam(required = false)SearchType searchType,
            @RequestParam(required = false)String searchValue,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
            ) throws Exception{

        return articleService.searchArticles(searchType, searchValue, pageable).map(ArticlesResponse::from);
    }

    @GetMapping("/fetch")
    public Page<ArticleWithPhotoWithCommentsResponse> articleWithPhotoWithCommentsResponsePage(
            @PageableDefault(size = 500, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return articleService.searchArticlesWithCommentWithPhoto(pageable).map(ArticleWithPhotoWithCommentsResponse::from);
    }

    @GetMapping("/mypage")
    public Page<ArticleWithPhotoWithCommentsResponse> articleWithPhotoWithCommentsResponseMyPage(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal ServicePrincipal servicePrincipal
    ) {
        return articleService.getMyArticles(pageable, servicePrincipal.toDto()).map(ArticleWithPhotoWithCommentsResponse::from);

    }

    //상세 게시글 가져오기
    @GetMapping("/{articleId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ArticleWithPhotoWithCommentsResponse articleDetails(@PathVariable Long articleId) throws Exception{
        return ArticleWithPhotoWithCommentsResponse.from(
                articleService.getArticleWithPhotoWithComments(articleId)
        );
    }

    //게시글 업로드
    @PostMapping
    public ResponseEntity<String> articleUploads(@Validated UploadRequest uploadRequest,
                                         BindingResult bindingResult,
                                         @AuthenticationPrincipal ServicePrincipal servicePrincipal
    ) throws IOException {

        if (bindingResult.hasErrors()) {
            // 검증 오류가 있는 경우 처리
            StringBuilder errorMessage = new StringBuilder();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessage.append(fieldError.getField())
                        .append(": ")
                        .append(fieldError.getDefaultMessage())
                        .append("; ");
            }

            return new ResponseEntity<String>(errorMessage.toString(),HttpStatus.BAD_REQUEST);
        }

        String storedName = createStoredName(uploadRequest.multipartFile().getOriginalFilename());
        s3UploaderService.uploadFile(uploadRequest.multipartFile(), storedName);
        articleService.saveArticle(uploadRequest.toDto(servicePrincipal.toDto(), storedName));

        return new ResponseEntity<String>("게시글 생성이 완료되었습니다.", HttpStatus.OK);
    }

    private String createStoredName(String fileName) {
        String extension = "";

        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }
        return UUID.randomUUID().toString() + "." + extension;
    }

    //게시글 수정
    @PutMapping("/{articleId}")
    @ResponseStatus(value = HttpStatus.OK)
    public String updateArticle (@PathVariable Long articleId,
                                 @AuthenticationPrincipal ServicePrincipal servicePrincipal,
                                 @Validated UploadRequest uploadRequest,
                                 BindingResult bindingResult) {

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

        articleService.updateArticle(articleId, uploadRequest.toDto(servicePrincipal.toDto()));
        return "게시글 정보가 수정되었습니다.";
    }

    //게시글 삭제
    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteArticle(@PathVariable Long articleId,
                                @AuthenticationPrincipal ServicePrincipal servicePrincipal) {
        articleService.deleteArticle(articleId, servicePrincipal.getName());

        return "게시물이 삭제되었습니다.";
    }
}
