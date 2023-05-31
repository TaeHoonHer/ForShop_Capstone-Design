package com.forshop.project.service;

import com.forshop.project.domain.UserAccount;
import com.forshop.project.dto.UserAccountDto;
import com.forshop.project.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비지니스 로직 - 회원")
@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @InjectMocks private UserAccountService sut;

    @Mock private UserAccountRepository userAccountRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @DisplayName("존재하는 회원 ID를 검색하면, 회원 데이터를 Optional로 반환한다.")
    @Test
    void givenExistentUserId_whenSearching_thenReturnsOptionalUserData() {
        String userId = "capstonegood@naver.com";
        given(userAccountRepository.findById(userId)).willReturn(Optional.of(createUserAccount(userId)));

        Optional<UserAccountDto> userAccountDto = sut.searchUser(userId);

        assertThat(userAccountDto).isPresent();
        then(userAccountRepository).should().findById(userId);
    }

    @DisplayName("존재하는 회원 ID를 검색하면, 회원 데이터를 Optional로 반환한다.")
    @Test
    void givenNonexistentUserId_whenSearching_thenReturnsOptionalUserData() {
        String userId = "wrong-user@naver.com";
        given(userAccountRepository.findById(userId)).willReturn(Optional.empty());

        Optional<UserAccountDto> userAccountDto = sut.searchUser(userId);

        assertThat(userAccountDto).isEmpty();
        then(userAccountRepository).should().findById(userId);
    }

    @DisplayName("회원 정보를 입력하면, 새로운 회원 정보를 저장하여 가입시키고 해당 회원 데이터를 리턴한다.")
    @Test
    void givenUserParams_whenSaving_thenSavesUserAccount() {

        //Given
        UserAccount userAccount = createUserAccount("hwang");
        UserAccount savedUserAccount = createSigningUpUserAccount("hwang");
        given(userAccountRepository.save(userAccount)).willReturn(savedUserAccount);
        given(passwordEncoder.encode(userAccount.getUserPassword())).willReturn(anyString());

        UserAccountDto result = sut.saveUser(
                userAccount.getUserId(),
                userAccount.getUserPassword(),
                userAccount.getEmail(),
                userAccount.getNickname()
        );

        assertThat(result)
                .hasFieldOrPropertyWithValue("userId", userAccount.getUserId())
                .hasFieldOrPropertyWithValue("userPassword", userAccount.getUserPassword())
                .hasFieldOrPropertyWithValue("email", userAccount.getEmail())
                .hasFieldOrPropertyWithValue("nickname", userAccount.getNickname())
                .hasFieldOrPropertyWithValue("createdBy", userAccount.getUserId())
                .hasFieldOrPropertyWithValue("modifiedBy", userAccount.getUserId());
        then(userAccountRepository).should().save(userAccount);
        then(passwordEncoder).should().encode(userAccount.getUserPassword());
    }

    private UserAccount createUserAccount(String userId) {
        return createUserAccount(userId, null);
    }

    private UserAccount createSigningUpUserAccount(String userId) {
        return createUserAccount(userId, userId);
    }

    private UserAccount createUserAccount(String userId, String createdBy) {
        return UserAccount.of(
                userId,
                "password",
                "capstone@email",
                "nickname",
                createdBy
        );
    }
}