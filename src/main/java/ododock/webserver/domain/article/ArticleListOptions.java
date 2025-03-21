package ododock.webserver.domain.article;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ododock.webserver.domain.ListOptions;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ArticleListOptions extends ListOptions {

    private String authorName;
    private Boolean visibility;

    // below fractional search
    private String title;
    private String body;
    private String category;
    private List<String> tags;

}
