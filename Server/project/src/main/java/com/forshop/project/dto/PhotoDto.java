package com.forshop.project.dto;

import com.forshop.project.domain.Photo;

import java.time.LocalDateTime;

public record PhotoDto (
        Long id,
        String storedName,
        String realName,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
){

    public static PhotoDto of(Long id, String storedName, String realName, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new PhotoDto(id, storedName, realName, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static PhotoDto of(String storedName, String realName) {
        return new PhotoDto(null, storedName, realName, null, null, null, null);
    }
    public static PhotoDto from(Photo entity) {
        return new PhotoDto(
                entity.getId(),
                entity.getStoredName(),
                entity.getRealName(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }
    public Photo toEntity() {
        return Photo.of(storedName, realName);
    }
}
