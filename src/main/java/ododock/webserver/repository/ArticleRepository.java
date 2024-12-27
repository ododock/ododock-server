package ododock.webserver.repository;

import ododock.webserver.domain.article.Article;
import ododock.webserver.domain.article.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findArticleByCategory(final Category category);

}
