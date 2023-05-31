package com.forshop.project.dto.response;

import com.forshop.project.dto.UserAccountDto;

import java.time.LocalDateTime;

public record UserResponse (
        String userId,
        String userEmail,
        String userNickname,
        LocalDateTime createdAt
        ){
    public static UserResponse of(String userId, String userEmail, String userNickname, LocalDateTime createdAt) {
        return new UserResponse(userId, userEmail, userNickname, createdAt);
    }

    public static UserResponse from(UserAccountDto dto) {
        String nickname = dto.nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.email();
        }

        return new UserResponse(
                dto.userId(),
                dto.email(),
                dto.nickname(),
                dto.createdAt()
        );
    }
}
