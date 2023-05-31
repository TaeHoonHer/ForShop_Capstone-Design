package com.forshop.project.dto.request;

import com.forshop.project.dto.UserAccountDto;
import jakarta.validation.constraints.*;

public record SignupRequest (
        @Size(min = 7, max = 25, message = "아이디는 7~25자 내외로 입력해주세요")
        @NotBlank(message = "아이디를 입력해주세요")
        String userId,

        @Size(min = 7, max = 30, message = "비밀번호는 8~30자로 입력해주세요")
        @NotBlank(message = "비밀번호를 입력해주세요")
        String userPassword,

        @NotBlank(message = "email을 입력해주세요.")
        @Pattern(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}", message = "유효한 이메일 주소를 입력해주세요.")
        String email,

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min=2, message = "닉네임이 너무 짧습니다.")
        String nickname){

    public static SignupRequest of(String userId, String userPassword, String email, String nickname) {
        return new SignupRequest(userId, userPassword, email, nickname);
    }

    public UserAccountDto toDto() {
        return UserAccountDto.of(
                userId,
                userPassword,
                email,
                nickname
        );
    }


}
