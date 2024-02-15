package ododock.webserver.service;

import lombok.AllArgsConstructor;
import ododock.webserver.repository.ArticleRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleService {

    final private ArticleRepository articleRepository;

    public void registerArticle() {

    }

}
