package ododock.webserver.web.v1alpha1.dto.account;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ododock.webserver.web.v1alpha1.dto.V1alpha1Base;
import org.springframework.lang.Nullable;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class V1alpha1SocialAccount extends V1alpha1Base {

    @Nullable
    private Long accountId;
    @Nullable
    private String provider;

}
