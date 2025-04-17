package ododock.webserver.domain.article;

import org.reactivestreams.Publisher;

public interface ArticleService {

    Publisher<Article> getArticle(String articleId);

    Publisher<Article> listArticles(Long accountId, ArticleListOptions listOptions);

    Publisher<Article> createArticle(Article article);

    Publisher<Article> updateArticle(String articleId, Article article);

    Publisher<Void> deleteArticle(String articleId);

}
