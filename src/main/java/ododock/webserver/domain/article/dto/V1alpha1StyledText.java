package ododock.webserver.domain.article.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class V1alpha1StyledText extends V1alpha1InlineContent {

    String type = "text";

}
