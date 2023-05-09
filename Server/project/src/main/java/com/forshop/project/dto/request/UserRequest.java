package com.forshop.project.dto.request;

public record UserRequest(
        String email,
        String password,
        String nickname
) {
}
