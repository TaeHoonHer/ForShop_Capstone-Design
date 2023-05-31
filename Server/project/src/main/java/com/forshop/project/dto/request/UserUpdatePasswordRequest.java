package com.forshop.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdatePasswordRequest(
        @Size(min = 7, max = 30, message = "비밀번호는 8~30자로 입력해주세요")
        @NotBlank(message = "기존 비밀번호를 입력해주세요")
        String checkPassword,

        @Size(min = 7, max = 30, message = "비밀번호는 8~30자로 입력해주세요")
        @NotBlank(message = "바꿀 비밀번호를 입력해주세요")
        String toBePassword
        ) {


}
