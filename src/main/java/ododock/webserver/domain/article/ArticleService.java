package ododock.webserver.domain.article;

import org.reactivestreams.Publisher;

public interface ArticleService {

    Publisher<Article> getArticle(Long articleId);

    Publisher<Article> listArticles();

    Publisher<Article> createArticle(Article article);

    Publisher<Article> updateArticle(Article article);

    Publisher<Article> deleteArticle(Article article);

}
