package ododock.webserver.web.v1alpha1.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.web.v1alpha1.dto.V1alpha1ListOptions;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class V1alpha1CategoryListOptions extends V1alpha1ListOptions {

    private String name;
    private String ownerId;
    private String ownerName;
    private Boolean visibility;
    private Integer position;

}
