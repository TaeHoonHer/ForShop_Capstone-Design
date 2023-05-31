package com.forshop.project.controller;

import com.forshop.project.dto.request.RequestVideoFiles;
import com.forshop.project.service.S3UploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/analyzing")
@RequiredArgsConstructor
public class AnalyzingController {

    private final S3UploaderService uploadService;

    @Value("${python.server.url}")
    private String pythonServerUrl;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(RequestVideoFiles requestFiles) {

        Map<String, String> fileNames = new HashMap<>();

        MultipartFile originalVideo = requestFiles.originalVideo();
        MultipartFile compareVideo = requestFiles.compareVideo();

        String originalVideoStoredName = createStoredName(originalVideo.getOriginalFilename());
        String compareVideoStoredName = createStoredName(compareVideo.getOriginalFilename());

        log.info("Starting upload of original and compare videos");

        uploadService.uploadFile(originalVideo, originalVideoStoredName);
        uploadService.uploadFile(compareVideo, compareVideoStoredName);

        log.info("Successfully uploaded original and compare videos");

        fileNames.put("original_video", originalVideoStoredName);
        fileNames.put("compare_video", compareVideoStoredName);

        for (Map.Entry<String, String> entry : fileNames.entrySet()) {
            log.info("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        WebClient webClient = WebClient.create(pythonServerUrl);

        log.info("Sending fileNames to python server");

        WebClient.ResponseSpec responseSpec = webClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(fileNames)
                .retrieve();

        log.info("Create WebClient.ResponseSpec");

        String response = responseSpec.bodyToMono(String.class).block();

        log.info("Response Message :{}", response);
        return ResponseEntity.ok(response);
    }

    private String createStoredName(String fileName) {
        String extension = "";

        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString();
    }


}
