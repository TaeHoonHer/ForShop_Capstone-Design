package com.forshop.project.controller;

import com.forshop.project.dto.request.SignInRequest;
import com.forshop.project.dto.request.SignupRequest;
import com.forshop.project.dto.request.UserUpdatePasswordRequest;
import com.forshop.project.dto.request.UserUpdateRequest;
import com.forshop.project.dto.response.JwtToken;
import com.forshop.project.dto.response.UserResponse;
import com.forshop.project.dto.security.ServicePrincipal;
import com.forshop.project.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserAccountService userAccountService;

    @GetMapping("/user")
    public ResponseEntity<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<UserResponse> userResponse = userAccountService.searchUser(username).map(UserResponse::from);

        if (userResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.ok(userResponse);
    }


    //회원가입
    @PostMapping("/signup")
    @ResponseStatus(value = HttpStatus.OK)
    public String signUp(@Validated @RequestBody SignupRequest dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessage.append(fieldError)
                        .append(": ")
                        .append(fieldError.getDefaultMessage())
                        .append("; ");
            }
            return errorMessage.toString();
        }
        userAccountService.saveUser(dto.userId(), dto.userPassword(), dto.email(), dto.nickname());
        return "회원 가입이 완료되었습니다.";
    }

    /**
     * 로그인 서비스
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JwtToken> loginSuccess(@Validated @RequestBody SignInRequest signInRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 검증 오류가 있는 경우 처리
            StringBuilder errorMessage = new StringBuilder();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessage.append(fieldError.getField())
                        .append(": ")
                        .append(fieldError.getDefaultMessage())
                        .append("; ");
            }
        }

        JwtToken token = userAccountService.login(signInRequest.userId(), signInRequest.userPassword());
        return ResponseEntity.ok(token);
    }

    /**
     * 회원 수정
     */
    @PutMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public String updateUser(@Validated @RequestBody UserUpdateRequest request, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            // 검증 오류가 있는 경우 처리
            StringBuilder errorMessage = new StringBuilder();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessage.append(fieldError.getField())
                        .append(": ")
                        .append(fieldError.getDefaultMessage())
                        .append("; ");
            }

            return errorMessage.toString();
        }

        userAccountService.updateUser(request.email(), request.nickname());
        return "회원 정보가 업데이트 되었습니다.";
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("/user/password")
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public String updatePassword(@Validated @RequestBody UserUpdatePasswordRequest request,
                                 BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            // 검증 오류가 있는 경우 처리
            StringBuilder errorMessage = new StringBuilder();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessage.append(fieldError.getField())
                        .append(": ")
                        .append(fieldError.getDefaultMessage())
                        .append("; ");
            }

            userAccountService.updatePassword(request.checkPassword(), request.toBePassword());

            return errorMessage.toString();
        }
        return "비밀번호가 업데이트 되었습니다.";
    }
}
