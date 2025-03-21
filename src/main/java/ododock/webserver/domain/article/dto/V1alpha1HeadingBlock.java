package ododock.webserver.domain.article.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class V1alpha1HeadingBlock extends V1alpha1BaseBlock {

    String type = "heading";
    V1alpha1HeadingBlockProps props;

    @Data
    @EqualsAndHashCode(callSuper = true)
    static class V1alpha1HeadingBlockProps extends V1alpha1DefaultProps {
        Long level;
    }

}
