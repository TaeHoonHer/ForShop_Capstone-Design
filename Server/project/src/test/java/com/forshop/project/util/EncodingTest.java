package com.forshop.project.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

public class EncodingTest {

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    @DisplayName("패스워드 인코딩 테스트")
    void givenPassword_whenEncoding_thenEncodedPassword() {
        String password = "password123";

        String encodedPassword = passwordEncoder.encode(password);

        assertThat(passwordEncoder.matches(password, encodedPassword)).isTrue();
    }
}
