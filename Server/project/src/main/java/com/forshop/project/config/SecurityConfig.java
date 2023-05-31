package com.forshop.project.config;


import com.forshop.project.dto.security.JwtAuthenticationFilter;
import com.forshop.project.dto.security.JwtTokenProvider;
import com.forshop.project.dto.security.KakaoOAuth2Response;
import com.forshop.project.dto.security.ServicePrincipal;
import com.forshop.project.service.UserAccountService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.UUID;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Spring Security 환경 설정을 구성하기 위한 클래스
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService,
            JwtTokenProvider jwtTokenProvider,
            AuthenticationSuccessHandler successHandler
            ) throws Exception {

        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable())
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable())
                .exceptionHandling(handle ->
                        handle.authenticationEntryPoint(((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Authentication Failed"))))
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                antMatcher(HttpMethod.GET, "/api/articles"),
                                antMatcher(HttpMethod.GET, "/api/articles/**"),
                                antMatcher(HttpMethod.POST, "/api/auth/login"),
                                antMatcher(HttpMethod.POST, "/api/auth/signup"),
                                antMatcher(HttpMethod.POST, "/api/articles"),
                                antMatcher("/oauth2/**")
                        )
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oAuth -> oAuth
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oAuth2UserService)
                        ).successHandler(successHandler)
                );

        return http.build();
    }



    @Bean
    public UserDetailsService userDetailsService(UserAccountService userAccountService) {
        return userId -> userAccountService
                .searchUser(userId)
                .map(ServicePrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + userId));
    }
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            UserAccountService userAccountService,
            PasswordEncoder passwordEncoder
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);//유저정보를 가져온다
            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId();  // 예상 값 = "kakao"
            String providerId = String.valueOf(kakaoResponse.id());
            String userId = registrationId + "_" + providerId;
            String dummyPassword = passwordEncoder.encode(UUID.randomUUID().toString());

            return userAccountService.searchUser(userId)
                    .map(ServicePrincipal::from)
                    .orElseGet(()->
                            ServicePrincipal.from(
                                    userAccountService.saveUser(
                                            userId,
                                            dummyPassword,
                                            kakaoResponse.email(),
                                            kakaoResponse.nickname()
                                    )
                            )
                    );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}