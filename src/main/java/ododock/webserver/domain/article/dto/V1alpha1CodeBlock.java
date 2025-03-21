package ododock.webserver.domain.article.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class V1alpha1CodeBlock extends V1alpha1BaseBlock {

    String type = "codeBlock";

    @Data
    @EqualsAndHashCode(callSuper = true)
    private static class V1alpha1CodeBlockProps extends V1alpha1DefaultProps {
        String language;
    }

}
