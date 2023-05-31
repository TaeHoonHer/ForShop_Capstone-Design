package com.forshop.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest (

        @NotBlank(message = "이메일을 입력해주세요.")
        @Pattern(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}", message = "유효한 이메일 주소를 입력해주세요.")
        String email,

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min=2, message = "닉네임이 너무 짧습니다.")
        String nickname
) {
}
