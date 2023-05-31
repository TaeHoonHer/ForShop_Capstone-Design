package com.forshop.project.dto.request;
import com.forshop.project.dto.ArticleDto;
import com.forshop.project.dto.HashtagDto;
import com.forshop.project.dto.PhotoDto;
import com.forshop.project.dto.UserAccountDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

public record UploadRequest (
        @NotBlank(message = "제목을 입력해주세요.")
        @Size(min = 7, max = 25, message = "제목은 7~20자 내외로 입력해주세요.")
        String title,
        @NotBlank(message = "본문을 입력해주세요.")
        @Size(min = 7, max = 1000, message = "내용은 7~1000자로 입력해주세요")
        String content,

        @NotNull(message = "사진을 입력해주세요.")
        MultipartFile multipartFile
)
{

    public static UploadRequest of(String title, String content, MultipartFile multipartFile) {
        return new UploadRequest(title, content, multipartFile);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos) {
        PhotoDto photoDto = photoDto(multipartFile);
        return ArticleDto.of(userAccountDto, photoDto, title, content, hashtagDtos);
    }
    public ArticleDto toDto(UserAccountDto userAccountDto) {
        PhotoDto photoDto = photoDto(multipartFile);
        return ArticleDto.of(userAccountDto, photoDto, title, content, null);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto, String storedName) {
        PhotoDto photoDto = photoDto(multipartFile, storedName);
        return ArticleDto.of(userAccountDto, photoDto, title, content, null);
    }

    private PhotoDto photoDto(MultipartFile multipartFile, String storedName) {
        String realName = multipartFile.getOriginalFilename();
        return PhotoDto.of(storedName, realName);
    }
    private PhotoDto photoDto(MultipartFile multipartFile) {
        String realName = multipartFile.getOriginalFilename();
        String storedName = UUID.randomUUID().toString();

        return PhotoDto.of(storedName, realName);
    }
}
