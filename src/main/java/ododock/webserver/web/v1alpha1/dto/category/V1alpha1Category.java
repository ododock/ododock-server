package ododock.webserver.web.v1alpha1.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.article.Category;
import ododock.webserver.domain.article.CategoryListOptions;
import ododock.webserver.web.v1alpha1.dto.V1alpha1Base;
import ododock.webserver.web.v1alpha1.dto.article.V1alpha1Article;
import ododock.webserver.web.v1alpha1.dto.article.V1alpha1ArticleSummary;
import org.springframework.lang.Nullable;

import java.util.List;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class V1alpha1Category extends V1alpha1Base {

    @Nullable
    private String id;
    private Long ownerAccountId;
    private String name;
    @PositiveOrZero
    private Integer position;
    private boolean visibility;
    @Nullable
    private List<V1alpha1Article> articles;

    @Nullable
    private List<V1alpha1ArticleSummary> articleSummaries;

    public static CategoryListOptions toDomainDto(V1alpha1CategoryListOptions controllerDto) {
        CategoryListOptions domainDto = new CategoryListOptions();
        domainDto.setPageOffset(controllerDto.getPageOffset());
        domainDto.setPageSize(controllerDto.getPageSize());

        return domainDto;
    }

    public Category toDomainDto() {
        return Category.builder()
                .id(this.id)
                .ownerAccountId(this.ownerAccountId)
                .name(this.name)
                .visibility(this.visibility)
                .position(this.position)
                .build();
    }

    public static V1alpha1Category toControllerDto(Category domainDto) {
        return V1alpha1Category.builder()
                .id(domainDto.getId())
                .name(domainDto.getName())
                .ownerAccountId(domainDto.getOwnerAccountId())
                .position(domainDto.getPosition())
                .visibility(domainDto.isVisibility())
                .createdAt(domainDto.getCreatedAt())
                .updatedAt(domainDto.getUpdatedAt())
                .build();
    }

    public static List<V1alpha1Category> toControllerDto(List<Category> domtainDto) {
        return domtainDto.stream()
                .map(V1alpha1Category::toControllerDto)
                .toList();
    }

}
