package ododock.webserver.web.v1alpha1.dto.article;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.article.Article;
import ododock.webserver.web.v1alpha1.dto.V1alpha1Base;

import javax.annotation.Nullable;
import java.util.Set;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class V1alpha1ArticleSummary extends V1alpha1Base {

    @Nullable
    private String id;
    private String title;
    private String body;
    private Boolean visibility;
    private Long ownerAccountId;
    @Nullable
    private String categoryId;
    private Set<String> tags;

    public static V1alpha1ArticleSummary toControllerDto(Article domainDto) {
        V1alpha1ArticleSummary controllerDto = new V1alpha1ArticleSummary();
        controllerDto.setId(domainDto.getId());
        controllerDto.setTitle(domainDto.getTitle());
        // todo fetch some of first body contents
        //  controllerDto.setBody(domainDto.getBody());
        controllerDto.setOwnerAccountId(domainDto.getOwnerAccountId());
        controllerDto.setCategoryId(domainDto.getCategoryId());
        controllerDto.setVisibility(domainDto.isVisibility());
        controllerDto.setTags(domainDto.getTags());
        controllerDto.setCreatedAt(domainDto.getCreatedDate());
        controllerDto.setUpdatedAt(domainDto.getLastModifiedAt());

        return controllerDto;
    }

}
