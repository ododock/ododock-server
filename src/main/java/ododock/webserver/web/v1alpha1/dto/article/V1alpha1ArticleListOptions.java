package ododock.webserver.web.v1alpha1.dto.article;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.article.ArticleListOptions;
import ododock.webserver.web.v1alpha1.dto.V1alpha1ListOptions;

import java.util.List;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class V1alpha1ArticleListOptions extends V1alpha1ListOptions {

    private String authorName;
    private Boolean visibility;

    // below fractional search
    private String title;
    private String body;
    private String category;
    private List<String> tags;

    public ArticleListOptions toDomainDto() {
        return ArticleListOptions.builder()
                .authorName(authorName)
                .visibility(visibility)
                .title(title)
                .body(body)
                .category(category)
                .tags(tags == null ? List.of() : tags)
                .build();
    }

}
