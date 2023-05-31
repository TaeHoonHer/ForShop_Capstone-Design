package com.forshop.project.repository;

import com.forshop.project.domain.ArticleComment;
import com.forshop.project.domain.QArticle;
import com.forshop.project.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;

public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticle> {
      List<ArticleComment> findByArticle_Id(Long articleId);
      void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);
      @Override
      default void customize(QuerydslBindings bindings, QArticle root) {
            bindings.excludeUnlistedProperties(true);
            bindings.including(root.content, root.createdAt, root.createdBy);
            bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
            bindings.bind(root.createdAt).first(DateTimeExpression::eq);
            bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
      }
}
