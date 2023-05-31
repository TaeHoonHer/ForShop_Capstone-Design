package com.forshop.project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        indexes = {
                @Index(columnList = "email", unique = true),
                @Index(columnList = "createdAt"),
                @Index(columnList = "createdBy")
        }
)
@Entity
public class UserAccount {

    @Id @Column(length = 50)
    private String userId;

    @Setter
    @Column(nullable = false)
    private String userPassword;

    @Setter @Column(length = 100)
    private String email;

    @Setter @Column(length = 100)
    private String nickname;

    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private UserAccount(String userId, String userPassword, String email, String nickname, String createdBy) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public static UserAccount of(String userId, String userPassword, String email, String nickname) {
        return new UserAccount(userId, userPassword, email, nickname, null);
    }

    public static UserAccount of(String userId, String userPassword, String email, String nickname, String createdBy) {
        return new UserAccount(userId, userPassword, email, nickname, createdBy);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return this.getUserId() != null && this.getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }
}
