package ododock.webserver.web.v1alpha1.dto.article;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.article.Article;
import ododock.webserver.domain.article.dto.V1alpha1BaseBlock;
import ododock.webserver.web.v1alpha1.dto.V1alpha1Base;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class V1alpha1Article extends V1alpha1Base {

    @Nullable
    private String id;
    private String title;
    private List<V1alpha1BaseBlock> body;
    private String excerpt;
    private Boolean visibility;
    private Long ownerAccountId;
    @Nullable
    private String categoryId;
    private Set<String> tags;

    public static V1alpha1Article toControllerDto(Article domainDto) {
        return V1alpha1Article.builder()
                .id(domainDto.getId())
                .title(domainDto.getTitle())
                .body(domainDto.getBody())
                .excerpt(domainDto.getExcerpt())
                .visibility(domainDto.isVisibility())
                .ownerAccountId(domainDto.getOwnerAccountId())
                .categoryId(domainDto.getCategoryId())
                .tags(domainDto.getTags() == null ? Set.of() : domainDto.getTags())
                .createdAt(domainDto.getCreatedAt())
                .updatedAt(domainDto.getUpdatedAt())
                .build();
    }

    public Article toDomainDto() {
        return Article.builder()
                .title(title)
                .body(body)
                .visibility(visibility)
                .ownerAccountId(ownerAccountId)
                .categoryId(categoryId)
                .tags(tags == null ? Set.of() : tags)
                .build();
    }

}