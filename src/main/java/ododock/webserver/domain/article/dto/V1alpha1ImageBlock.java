package ododock.webserver.domain.article.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class V1alpha1ImageBlock extends V1alpha1BaseBlock {

    String type = "image";
    V1alpha1ImageBlockProps props;

    @Data
    @EqualsAndHashCode(callSuper = true)
    private static class V1alpha1ImageBlockProps extends V1alpha1DefaultProps {
        String url;
        String caption;
        Long previewWidth;
    }

}
