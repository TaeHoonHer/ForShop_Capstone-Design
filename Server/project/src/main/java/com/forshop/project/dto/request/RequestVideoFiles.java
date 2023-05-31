package com.forshop.project.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record RequestVideoFiles(
        MultipartFile originalVideo,
        MultipartFile compareVideo
) {

    public static RequestVideoFiles of(MultipartFile originalVideo, MultipartFile compareVideo) {
        return new RequestVideoFiles(originalVideo, compareVideo);
    }
}
