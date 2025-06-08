package ododock.webserver.domain.article;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.ListOptions;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ArticleListOptions extends ListOptions {

    private String authorName;
    private Boolean visibility;
    private String title;
    private String keyword;
    private String categoryId;
    private List<String> tags;

}
