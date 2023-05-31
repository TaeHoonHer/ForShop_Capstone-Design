package com.forshop.project.repository;

import com.forshop.project.domain.Hashtag;
import com.forshop.project.repository.querydsl.HashtagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HashtagRepository extends
        JpaRepository<Hashtag, Long>,
        HashtagRepositoryCustom,
        QuerydslPredicateExecutor<Hashtag> {
    Optional<Hashtag> findByHashtagName(String hashtagName);

//    List<Hashtag> findByHashtagNamesIn(Set<String> hashtagNames);
    List<Hashtag> findHashtagsByHashtagNameIn(Set<String> hashtagNames);
}
