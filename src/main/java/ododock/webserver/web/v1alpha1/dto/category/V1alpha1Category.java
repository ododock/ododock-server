package ododock.webserver.web.v1alpha1.dto.category;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.article.Category;
import ododock.webserver.domain.article.CategoryListOptions;
import ododock.webserver.web.v1alpha1.dto.V1alpha1Base;

import java.util.List;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class V1alpha1Category extends V1alpha1Base {

    private String id;
    private Long ownerAccountId;
    private String name;
    private Integer position;
    private boolean visibility;

    public static CategoryListOptions toDomainDto(V1alpha1CategoryListOptions controllerDto) {
        CategoryListOptions domainDto = new CategoryListOptions();
        domainDto.setPageOffset(controllerDto.getPageOffset());
        domainDto.setPageSize(controllerDto.getPageSize());

        return domainDto;
    }

    public Category toDomainDto() {
        return Category.builder()
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
                .position(domainDto.getPosition())
                .visibility(domainDto.isVisibility())
                .createdAt(domainDto.getCreatedDate())
                .updatedAt(domainDto.getLastModifiedAt())
                .build();
    }

    public static List<V1alpha1Category> toControllerDto(List<Category> domtainDto) {
        return domtainDto.stream()
                .map(V1alpha1Category::toControllerDto)
                .toList();
    }

}
