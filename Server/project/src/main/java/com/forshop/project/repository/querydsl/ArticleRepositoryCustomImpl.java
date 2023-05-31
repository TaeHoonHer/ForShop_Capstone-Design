package com.forshop.project.repository.querydsl;

import com.forshop.project.domain.*;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Collection;
import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable) {
        QHashtag hashtag = QHashtag.hashtag;
        QArticle article = QArticle.article;

        JPQLQuery<Article> query = from(article)
                .innerJoin(article.hashtags, hashtag)
                .where(hashtag.hashtagName.in(hashtagNames));

        List<Article> articles = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(articles, pageable, query.fetchCount());
    }

    @Override
    public Page<Article> findByArticleWithCommentsWithPhoto(Pageable pageable) {
        QArticle article = QArticle.article;
        QPhoto photo = QPhoto.photo;
        QArticleComment articleComment = QArticleComment.articleComment;

        JPQLQuery<Article> articleJPQLQuery = from(article)
                .innerJoin(article.photo, photo).fetchJoin()
                .innerJoin(article.articleComments, articleComment).fetchJoin();
        List<Article> articles = getQuerydsl().applyPagination(pageable, articleJPQLQuery).fetch();

        return new PageImpl<>(articles, pageable, articleJPQLQuery.fetchCount());
    }
}
