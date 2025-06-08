package ododock.webserver.web.v1alpha1.dto.article;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.article.ArticleListOptions;
import ododock.webserver.web.v1alpha1.dto.V1alpha1ListOptions;
import org.springframework.lang.Nullable;

import java.util.List;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class V1alpha1ArticleListOptions extends V1alpha1ListOptions {

    @Nullable
    private String authorName;
    @Nullable
    private Boolean visibility;
    @Nullable
    private String title;
    @Nullable
    private String keyword;
    @Nullable
    private String categoryId;
    @Nullable
    private List<String> tags;

    public ArticleListOptions toDomainDto() {
        return ArticleListOptions.builder()
                .size(getSize())
                .sort(getSort())
                .sortKeys(getSortKeys())
                .authorName(authorName)
                .visibility(visibility)
                .title(title)
                .keyword(keyword)
                .categoryId(categoryId)
                .tags(tags == null ? List.of() : tags)
                .build();
    }

}
