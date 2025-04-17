package ododock.webserver.domain.article;

import lombok.RequiredArgsConstructor;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.repository.reactive.ArticleRepository;
import ododock.webserver.web.ResourceNotFoundException;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class ReactiveArticleService implements ArticleService {

    private final AccountRepository accountRepository;
    private final ArticleRepository articleRepository;

    @Override
    public Publisher<Article> getArticle(String articleId) {
        return articleRepository.findById(articleId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Article.class, articleId)));
    }

    @Override
    public Publisher<Article> listArticles(Long accountId, ArticleListOptions listOptions) {
        return Mono.fromCallable(() -> accountRepository.existsById(accountId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(exists -> {
                    if (!exists) {
                        return Flux.error(new ResourceNotFoundException(Article.class, accountId));
                    }
                    return articleRepository.findArticlesByOwnerAccountId(accountId);
                            // todo apply list options
//                            .filter(Article::isVisibility);
                });
    }

    @Override
    public Publisher<Article> createArticle(Article article) {
        return Mono.fromCallable(() -> accountRepository.existsById(article.getOwnerAccountId()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ResourceNotFoundException(Article.class, article.getOwnerAccountId()));
                    }
                    return articleRepository.save(article);
                });
    }

    @Override
    public Publisher<Article> updateArticle(String articleId, Article article) {
        return Mono.fromCallable(() -> accountRepository.existsById(article.getOwnerAccountId()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ResourceNotFoundException(Article.class, article.getOwnerAccountId()));
                    }
                    return articleRepository.findById(articleId)
                            .switchIfEmpty(Mono.error(new ResourceNotFoundException(Article.class, articleId)))
                            .flatMap(foundArticle -> {
                                foundArticle.updateTitle(article.getTitle());
                                foundArticle.updateVisibility(article.isVisibility());
                                foundArticle.updateBody(article.getBody());
                                foundArticle.updateTags(article.getTags());
                                foundArticle.updateCategory(article.getCategoryId());
                                return articleRepository.save(foundArticle);
                            });
                });
    }

    @Override
    public Mono<Void> deleteArticle(String articleId) {
        return articleRepository.findById(articleId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(Article.class, articleId)))
                .flatMap(article -> articleRepository.deleteById(articleId))
                .then();
    }

}
