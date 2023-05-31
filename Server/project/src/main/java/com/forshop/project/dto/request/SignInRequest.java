package com.forshop.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @Size(min = 7, max = 25, message = "아이디는 7~25자 내외로 입력해주세요")
        @NotBlank(message = "아이디를 입력해주세요")
        String userId,

        @Size(min = 8, max = 30, message = "비밀번호는 8~30자로 입력해주세요")
        @NotBlank(message = "비밀번호를 입력해주세요")
        String userPassword
) {
    public static SignInRequest of(String userId, String userPassword) {
        return new SignInRequest(userId, userPassword);
    }
}
