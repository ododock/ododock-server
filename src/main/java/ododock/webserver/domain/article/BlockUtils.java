package ododock.webserver.domain.article;

import ododock.webserver.domain.article.dto.V1alpha1BaseBlock;
import ododock.webserver.domain.article.dto.V1alpha1InlineContent;

public abstract class BlockUtils {

    private final static String TEXT = "text";

    public static boolean hasTextContent(V1alpha1BaseBlock block) {
        return block.getType().equals(TEXT)
                && block.getContent() != null
                && !block.getContent().isEmpty();
    }

    public static boolean hasTextContent(V1alpha1InlineContent content) {
        return content.getType().equals(TEXT)
                && content.getText() != null
                && !content.getText().isBlank();
    }

    public static boolean hasChildrenContent(V1alpha1BaseBlock block) {
        return block.getChildren() != null && !block.getChildren().isEmpty();
    }

}
