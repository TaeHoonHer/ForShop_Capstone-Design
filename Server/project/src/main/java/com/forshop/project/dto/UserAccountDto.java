package com.forshop.project.dto;

import com.forshop.project.domain.UserAccount;

import java.time.LocalDateTime;

public record UserAccountDto (
        Long id,
        String email,
        String userPassword,
        String nickname,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
){

    public static UserAccountDto of(Long id, String email, String userPassword, String nickname, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new UserAccountDto(id, email, userPassword, nickname, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static UserAccountDto of(String email, String userPassword, String nickname) {
        return new UserAccountDto(null, email, userPassword, nickname, null, null, null, null);
    }

    public static UserAccountDto of(Long id, String email, String userPassword, String nickname) {
        return new UserAccountDto(id, email, userPassword, nickname, null, null, null, null);
    }

    public static  UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getId(),
                entity.getEmail(),
                entity.getUserPassword(),
                entity.getNickname(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public UserAccount toEntity() {
        return UserAccount.of(
                userPassword,
                email,
                nickname
        );
    }
}
