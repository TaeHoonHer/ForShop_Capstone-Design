package com.forshop.project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        indexes = {
                @Index(columnList = "email", unique = true),
                @Index(columnList = "createdAt"),
                @Index(columnList = "createdBy")
        }
)
@Entity
public class UserAccount extends AuditingFields{

    @Id @GeneratedValue
    private Long id;

    @Setter @Column(length = 100, nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String userPassword;


    @Setter @Column(length = 100)
    private String nickname;

    private UserAccount(String userPassword, String email, String nickname) {
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
    }

    public static UserAccount of(String userPassword, String email, String nickname) {
        return new UserAccount(userPassword, email, nickname);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return this.getEmail() != null && this.getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEmail());
    }
}
