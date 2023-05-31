package com.forshop.project.repository.querydsl;

import com.forshop.project.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface ArticleRepositoryCustom {

    Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);
    Page<Article> findByArticleWithCommentsWithPhoto(Pageable pageable);
}
