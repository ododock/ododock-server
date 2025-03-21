package ododock.webserver.domain.article;

import org.reactivestreams.Publisher;

public interface ArticleService {

    Publisher<Article> getArticle(String articleId);

    Publisher<Article> listArticles(Long accountId, ArticleListOptions listOptions);

    Publisher<Article> createArticle(Article article);

    Publisher<Article> updateArticle(Article article);

    Publisher<Void> deleteArticle(String articleId);

}
