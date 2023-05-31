package com.forshop.project.repository.querydsl;

import com.forshop.project.domain.Hashtag;

import java.util.List;

public interface HashtagRepositoryCustom {

    List<String> findAllHashtagNames();
}
