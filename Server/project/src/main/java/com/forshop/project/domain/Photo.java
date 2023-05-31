package com.forshop.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "storedName", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
public class Photo extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(length = 500, unique = true)
    private String storedName;
    @Setter @Column(length = 500)
    private String realName;

    protected Photo(){}
    private Photo(String storedName, String realName) {
        this.storedName = storedName;
        this.realName = realName;
    }

    public static Photo of(String storedName, String realName) {
        return new Photo(storedName, realName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
