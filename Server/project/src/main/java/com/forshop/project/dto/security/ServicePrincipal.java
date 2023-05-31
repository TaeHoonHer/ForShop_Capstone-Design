package com.forshop.project.dto.security;

import com.forshop.project.domain.type.RoleType;
import com.forshop.project.dto.UserAccountDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record ServicePrincipal(
        String userId,
        String password,
        String email,
        Collection<? extends GrantedAuthority> authorities,
        String nickname,
        Map<String, Object> oAuth2Attributes
) implements UserDetails, OAuth2User {

    public static ServicePrincipal of(String userId, String password, String email, String nickname) {

        return of(userId, password, email, nickname, Map.of());
    }

    public static ServicePrincipal of(String userId, String password, String email, String nickname, Map<String, Object> oAuth2Attributes) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new ServicePrincipal(
                userId,
                password,
                email,
                roleTypes.stream().map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                nickname,
                oAuth2Attributes
        );
    }

    public static ServicePrincipal from(UserAccountDto dto) {
        return ServicePrincipal.of(
                dto.userId(),
                dto.userPassword(),
                dto.email(),
                dto.nickname()
        );
    }

    public static ServicePrincipal of(UserAccountDto dto) {
        return of(
                dto.userId(),
                dto.userPassword(),
                dto.email(),
                dto.nickname(),
                Map.of());
    }

    public UserAccountDto toDto() {
        return UserAccountDto.of(
                userId,
                password,
                email,
                nickname
        );
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return userId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Attributes;
    }

}
