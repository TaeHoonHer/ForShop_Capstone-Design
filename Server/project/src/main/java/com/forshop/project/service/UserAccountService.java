package com.forshop.project.service;

import com.forshop.project.domain.UserAccount;
import com.forshop.project.dto.UserAccountDto;
import com.forshop.project.dto.response.JwtToken;
import com.forshop.project.dto.security.JwtTokenProvider;
import com.forshop.project.dto.security.ServicePrincipal;
import com.forshop.project.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public Optional<UserAccountDto> searchUser(String userId) {
        return userAccountRepository.findById(userId)
                .map(UserAccountDto::from);
    }

    public UserAccountDto saveUser(String userId, String userPassword, String email, String nickname) {
        if (userAccountRepository.existsById(userId)) {
            throw new IllegalArgumentException("ID is already taken");
        }
        String encodedPassword = getEncodedPassword(userPassword);
        return UserAccountDto.from(
                userAccountRepository.save(UserAccount.of(userId, encodedPassword, email, nickname, userId))
        );
    }

    public void updateUser(String email, String nickname) throws Exception {
        UserAccount userAccount = userAccountRepository.findById(getLoginUserId()).orElseThrow(() -> new Exception("회원이 존재하지 않습니다"));

        userAccount.setEmail(email);
        userAccount.setNickname(nickname);
    }

    public void updatePassword(String checkPassword, String toBePassword) throws Exception {
        UserAccount userAccount = userAccountRepository.findById(getLoginUserId()).orElseThrow(() -> new Exception("회원이 존재하지 않습니다"));

        if(!passwordEncoder.matches(checkPassword, userAccount.getUserPassword()) ) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        userAccount.setUserPassword(getEncodedPassword(toBePassword));
    }

    private String getLoginUserId() {
        ServicePrincipal principal = (ServicePrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        return username;
    }


    private String getEncodedPassword(String userPassword) {
        return passwordEncoder.encode(userPassword);
    }

    public JwtToken login(String userId, String userPassword) {
        //Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, userPassword);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken token = jwtTokenProvider.generateToken(authentication);
        return token;
    }
}
