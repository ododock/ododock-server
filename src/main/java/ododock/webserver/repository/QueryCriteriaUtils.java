package ododock.webserver.repository;

import ododock.webserver.domain.article.ArticleListOptions;
import ododock.webserver.domain.article.Tag;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public abstract class QueryCriteriaUtils {

    public static Query queryWith(ArticleListOptions listOptions) {
        Query query = new Query();

        if (listOptions.getAuthorName() != null) {
            Criteria criteria = Criteria.where("authorName").is(listOptions.getAuthorName());
            query.addCriteria(criteria);
        }

        if (listOptions.getVisibility() != null) {
            Criteria criteria = Criteria.where("visibility").is(listOptions.getVisibility());
            query.addCriteria(criteria);
        }

        if (listOptions.getTitle() != null && !listOptions.getTitle().isEmpty()) {
            Criteria criteria = Criteria.where("title").is(listOptions.getTitle());
            query.addCriteria(criteria);
        }

        if (listOptions.getKeyword() != null && !listOptions.getKeyword().isBlank()) {
            String keyword = listOptions.getKeyword().trim();
            String[] words = keyword.split("\\s+");

            List<Criteria> keywordRegexCriteria = Arrays.stream(words)
                    .map(word -> Criteria.where("plainText")
                            .regex(".*" + Pattern.quote(word) + ".*"))
                    .toList();

            query.addCriteria(new Criteria().andOperator(keywordRegexCriteria));
        }

        if (listOptions.getCategoryId() != null) {
            Criteria criteria = Criteria.where("categoryId").is(listOptions.getCategoryId());
            query.addCriteria(criteria);
        }

        if (listOptions.getTags() != null && !listOptions.getTags().isEmpty()) {
            List<Tag> tags = listOptions.getTags()
                    .stream().map(Tag::new).toList();

            List<Criteria> test = tags.stream().map(t -> Criteria.where("tags").is(t)).toList();

            Criteria criteria = new Criteria().andOperator(test);
            query.addCriteria(criteria);
        }

        return query;
    }

}
